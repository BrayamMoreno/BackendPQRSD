package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.repository.PersonasRepository;

@Service
public class PersonasService extends GenericService<Personas, Long>{

    private final PersonasRepository repository;

    public PersonasService(PersonasRepository repository){
        super(repository);
        this.repository = repository;
    }

    public Personas findByTipoDocAndNumDoc(Long tipoDoc, String numDoc){
        return repository.findByTipoDocAndNumDoc(tipoDoc, numDoc).orElse(null);
    }
}
