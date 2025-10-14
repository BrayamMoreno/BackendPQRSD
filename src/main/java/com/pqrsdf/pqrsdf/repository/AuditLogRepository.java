package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pqrsdf.pqrsdf.models.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
