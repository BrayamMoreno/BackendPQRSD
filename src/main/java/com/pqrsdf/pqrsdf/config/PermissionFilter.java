package com.pqrsdf.pqrsdf.config;

import java.io.IOException;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1. Excluir rutas de autenticación (/api/auth/**)
        if (path.startsWith("/api/auth/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Validar solo si el usuario está autenticado y no es anónimo
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()) {

            // Determinar el recurso (ej: usuarios, roles, pqs...)
            String recurso = extractResource(path);

            // Mapear acción a partir del método HTTP
            String accion = mapHttpMethodToAction(method, path);

            // Construir permiso esperado
            String permisoNecesario = recurso + "_" + accion;

            boolean tienePermiso = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(permisoNecesario));

            if (!tienePermiso) {
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

    private String mapHttpMethodToAction(String method, String path) {
        if ("GET".equals(method)) {
            // Si la ruta contiene "download", se considera acción "descargar"
            if (path != null && path.toLowerCase().contains("download")) {
                return "descargar";
            }
            return "leer";
        }

        return switch (method) {
            case "POST" -> "agregar";
            case "PUT", "PATCH" -> "modificar";
            case "DELETE" -> "eliminar";
            default -> "otro";
        };
    }
}
