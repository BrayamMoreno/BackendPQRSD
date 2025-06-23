package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TipoDoc;
import com.pqrsdf.pqrsdf.repository.TipoDocRepository;

@Service
public class TipoDocService extends GenericService<TipoDoc, Long>{
    public TipoDocService(TipoDocRepository repository){
        super(repository);
    }
}
