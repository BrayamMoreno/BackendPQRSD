package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.SeguimientosPq;
import com.pqrsdf.pqrsdf.repository.SeguimientosPqRepository;

@Service
public class SeguimientosPqService extends GenericService<SeguimientosPq, Long>{
    public SeguimientosPqService(SeguimientosPqRepository repository){
        super(repository);
    }
}
