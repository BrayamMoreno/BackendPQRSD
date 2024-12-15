package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TiposPersonas;
import com.pqrsdf.pqrsdf.repository.TiposPersonasRepository;

@Service
public class TiposPersonasService extends GenericService<TiposPersonas, Long>{
    public TiposPersonasService(TiposPersonasRepository repository){
        super(repository);
    }
}
