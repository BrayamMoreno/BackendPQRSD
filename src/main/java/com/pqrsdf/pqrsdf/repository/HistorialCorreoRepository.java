package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.HistorialCorreos;

@Repository
public interface HistorialCorreoRepository extends GenericRepository<HistorialCorreos, Long> {
    
}
