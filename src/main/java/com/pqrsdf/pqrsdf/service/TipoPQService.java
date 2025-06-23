package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TipoPQ;
import com.pqrsdf.pqrsdf.repository.TipoPQRepository;

@Service
public class TipoPQService extends GenericService<TipoPQ, Long>{
    public TipoPQService(TipoPQRepository repository){
        super(repository);
    }
}
