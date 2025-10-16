package com.pqrsdf.pqrsdf.components;

import com.pqrsdf.pqrsdf.models.AuditLog;
import com.pqrsdf.pqrsdf.repository.AuditLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;

public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditLogRepository auditRepo;

    public AuditLoggingFilter(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Continuar con la cadena de filtros / ejecución del endpoint
        filterChain.doFilter(request, response);

        String path = request.getRequestURI();
        String method = request.getMethod();

        AuditLog log = new AuditLog();
        log.setMethod(method);
        log.setEndpoint(path);
        log.setStatusCode(response.getStatus());
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        // Usuario autenticado (si aplica)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.setUsername(auth.getName());
        }

        // Acción: opcional, puedes inferirla del endpoint o método HTTP
        log.setAction(createMethod(method, path));

        // Guardar log
        auditRepo.save(log);
    }

    public String createMethod(String method, String path) {
        String recurso = extractResource(path);
        String accion = mapHttpMethodToAction(method, path);

        return recurso + "_" + accion;
    }

    private String mapHttpMethodToAction(String method, String path) {
        if ("GET".equals(method)) {
            if (path != null && path.toLowerCase().contains("download")) {
                return "descargar";
            }
            return "leer";
        }

        if ("POST".equals(method)) {
            if (path != null) {
                String lowerPath = path.toLowerCase();
                if (lowerPath.contains("login"))
                    return "iniciar_sesion";
                if (lowerPath.contains("logout"))
                    return "cerrar_sesion";
                if (lowerPath.contains("renew"))
                    return "renovar_sesion";
                if (lowerPath.contains("register"))
                    return "registrar_usuario";
            }
            return "crear";
        }

        return switch (method) {
            case "PUT", "PATCH" -> "modificar";
            case "DELETE" -> "eliminar";
            default -> "otro";
        };
    }

    private String extractResource(String path) {
        String[] parts = path.split("/");
        return parts.length > 2 ? parts[2] : "desconocido";
    }

}
