package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;

@Service
public class HistorialEstadoService extends GenericService<HistorialEstadoPQ, Long> {

    public HistorialEstadoService(HistorialEstadosRespository repository) {
        super(repository);
    }

}
