package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;

@Service
public class HistoralEstadoService extends GenericService<HistorialEstadoPQ, Long> {
    public HistoralEstadoService(HistorialEstadosRespository repository) {
        super(repository);
    }
}
