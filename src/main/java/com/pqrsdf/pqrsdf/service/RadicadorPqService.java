package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.RadicadorPq;
import com.pqrsdf.pqrsdf.repository.RadicadorPqRepository;

@Service
public class RadicadorPqService extends GenericService<RadicadorPq, Long>{
    public RadicadorPqService(RadicadorPqRepository repository){
        super(repository);
    }
}
