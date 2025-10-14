package com.pqrsdf.pqrsdf.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pqrsdf.pqrsdf.models.AuditLog;
import com.pqrsdf.pqrsdf.repository.AuditLogRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditLogRepository auditRepo;

    public AuditLoggingFilter(AuditLogRepository auditRepo) { // ✅ inyección por constructor
        this.auditRepo = auditRepo;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        AuditLog log = new AuditLog();
        log.setMethod(request.getMethod());
        log.setEndpoint(request.getRequestURI());
        log.setStatusCode(wrappedResponse.getStatus());
        log.setTimestamp(LocalDateTime.now());

        // Usuario autenticado (si aplica)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.setUsername(auth.getName());
        }

        // Intentar convertir el body a JSON
        try {
            String reqBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!reqBody.isBlank()) {
                log.setRequestBody(objectMapper.readValue(reqBody, Object.class));
            }
        } catch (Exception ignored) {
        }

        try {
            String respBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!respBody.isBlank()) {
                log.setResponseBody(objectMapper.readValue(respBody, Object.class));
            }
        } catch (Exception ignored) {
        }

        auditRepo.save(log);
        wrappedResponse.copyBodyToResponse();
    }
}
