package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.EstadoPQ;
import com.pqrsdf.pqrsdf.repository.EstadoPQRepository;

@Service
public class EstadoPQService extends GenericService<EstadoPQ, Long>{
    public EstadoPQService(EstadoPQRepository repository){
        super(repository);
    }
}
