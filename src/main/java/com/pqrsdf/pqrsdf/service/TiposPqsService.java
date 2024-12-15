package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TiposPqs;
import com.pqrsdf.pqrsdf.repository.TiposPqsRepository;

@Service
public class TiposPqsService extends GenericService<TiposPqs, Long>{
    public TiposPqsService(TiposPqsRepository repository){
        super(repository);
    }
}
