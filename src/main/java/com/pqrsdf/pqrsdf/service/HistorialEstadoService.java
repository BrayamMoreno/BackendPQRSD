package com.pqrsdf.pqrsdf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;

@Service
public class HistorialEstadoService extends GenericService<HistorialEstadoPQ, Long> {
    
    private final HistorialEstadosRespository repository;

    public HistorialEstadoService(HistorialEstadosRespository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<HistorialEstadoPQ> findByPqId(Long pqId) {
        return repository.findByPqId(pqId);
    }
}
