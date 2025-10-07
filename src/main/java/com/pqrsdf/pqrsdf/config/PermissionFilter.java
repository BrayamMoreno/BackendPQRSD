package com.pqrsdf.pqrsdf.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PermissionFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(PermissionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 🚨 1. Excluir rutas de autenticación (/api/auth/**)
        if (path.startsWith("/api/auth/")) {
            log.debug("PermissionFilter - Skipping for auth endpoint: {} {}", method, path);
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 🚨 2. Validar solo si el usuario está autenticado y no es anónimo
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()) {

            log.debug("PermissionFilter - Checking permissions for user: {}", authentication.getName());

            // Determinar el recurso (ej: usuarios, roles, pqs...)
            String recurso = extractResource(path);

            // Mapear acción a partir del método HTTP
            String accion = mapHttpMethodToAction(method);

            // Construir permiso esperado
            String permisoNecesario = recurso + "_" + accion;
            log.debug("PermissionFilter - Required permission: {}", permisoNecesario);

            boolean tienePermiso = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(permisoNecesario));

            if (!tienePermiso) {
                log.warn("PermissionFilter - Access denied. Missing permission: {}", permisoNecesario);
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No tienes el permiso necesario: " + permisoNecesario);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractResource(String path) {
        String[] parts = path.split("/");
        return parts.length > 2 ? parts[2] : "desconocido";
    }

    private String mapHttpMethodToAction(String method) {
        return switch (method) {
            case "GET" -> "leer";
            case "POST" -> "crear";
            case "PUT", "PATCH" -> "modificar";
            case "DELETE" -> "eliminar";
            default -> "otro";
        };
    }
}
