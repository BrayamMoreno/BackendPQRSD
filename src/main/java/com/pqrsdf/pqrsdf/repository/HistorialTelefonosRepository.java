package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.HistorialTelefonos;

@Repository
public interface HistorialTelefonosRepository extends GenericRepository<HistorialTelefonos, Long> {
    
}
