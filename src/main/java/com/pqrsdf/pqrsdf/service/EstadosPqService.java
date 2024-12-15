package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.EstadosPq;
import com.pqrsdf.pqrsdf.repository.EstadosPqRepository;

@Service
public class EstadosPqService extends GenericService<EstadosPq, Long>{
    public EstadosPqService(EstadosPqRepository repository){
        super(repository);
    }
}
