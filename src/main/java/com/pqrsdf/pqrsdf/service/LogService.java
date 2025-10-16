package com.pqrsdf.pqrsdf.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.models.AuditLog;
import com.pqrsdf.pqrsdf.repository.AuditLogRepository;

@Service
public class LogService {

    private final AuditLogRepository repository;

    public LogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public Page<AuditLog> getLogs(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
