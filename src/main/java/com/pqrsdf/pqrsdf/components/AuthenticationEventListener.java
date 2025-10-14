package com.pqrsdf.pqrsdf.components;

import com.pqrsdf.pqrsdf.models.AuditLog;
import com.pqrsdf.pqrsdf.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthenticationEventListener {

    @Autowired
    private AuditLogRepository auditRepo;

    @EventListener
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        AuditLog log = new AuditLog();
        log.setUsername(event.getAuthentication().getName());
        log.setMethod("LOGIN");
        log.setEndpoint("/login");
        log.setTimestamp(LocalDateTime.now());
        log.setStatusCode(200);
        log.setRequestBody("Login exitoso");
        auditRepo.save(log);
    }

    @EventListener
    public void handleLoginFailure(AbstractAuthenticationFailureEvent event) {
        AuditLog log = new AuditLog();
        log.setUsername(event.getAuthentication().getName());
        log.setMethod("LOGIN");
        log.setEndpoint("/login");
        log.setTimestamp(LocalDateTime.now());
        log.setStatusCode(401);
        log.setRequestBody("Login fallido");
        auditRepo.save(log);
    }
}
