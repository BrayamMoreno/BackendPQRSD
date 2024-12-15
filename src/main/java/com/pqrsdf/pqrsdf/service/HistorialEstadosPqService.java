package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadosPq;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosPqRepository;

@Service
public class HistorialEstadosPqService extends GenericService<HistorialEstadosPq, Long>{
    public HistorialEstadosPqService(HistorialEstadosPqRepository repository){
        super(repository);
    }
}
