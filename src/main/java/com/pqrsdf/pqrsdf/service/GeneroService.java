package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Genero;
import com.pqrsdf.pqrsdf.repository.GeneroRepository;

@Service
public class GeneroService extends GenericService<Genero, Long>{
    public GeneroService(GeneroRepository repository){
        super(repository);
    }
}
