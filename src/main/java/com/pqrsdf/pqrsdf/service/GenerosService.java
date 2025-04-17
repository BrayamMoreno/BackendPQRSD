package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Generos;
import com.pqrsdf.pqrsdf.repository.GenerosRepository;

@Service
public class GenerosService extends GenericService<Generos, Long>{
    public GenerosService(GenerosRepository repository){
        super(repository);
    }
}
