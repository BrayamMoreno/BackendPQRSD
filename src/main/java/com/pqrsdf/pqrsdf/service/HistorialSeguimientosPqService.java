package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialSeguimientosPq;
import com.pqrsdf.pqrsdf.repository.HistorialSeguimientosPqRepository;

@Service
public class HistorialSeguimientosPqService extends GenericService<HistorialSeguimientosPq, Long>{
    public HistorialSeguimientosPqService(HistorialSeguimientosPqRepository repository ){
        super(repository);
    }
}
