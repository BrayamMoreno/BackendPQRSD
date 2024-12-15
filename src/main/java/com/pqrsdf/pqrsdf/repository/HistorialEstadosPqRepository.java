package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.HistorialEstadosPq;

@Repository
public interface HistorialEstadosPqRepository extends GenericRepository<HistorialEstadosPq, Long>{
    
}
