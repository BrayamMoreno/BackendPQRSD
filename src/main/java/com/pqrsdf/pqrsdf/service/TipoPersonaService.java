package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TipoPersona;
import com.pqrsdf.pqrsdf.repository.TipoPersonaRepository;

@Service
public class TipoPersonaService extends GenericService<TipoPersona, Long>{
    public TipoPersonaService(TipoPersonaRepository repository){
        super(repository);
    }
}
