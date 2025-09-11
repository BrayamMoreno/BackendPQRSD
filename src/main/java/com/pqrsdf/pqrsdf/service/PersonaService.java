package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.repository.PersonaRepository;
@Service
public class PersonaService extends GenericService<Persona, Long>{

    public PersonaService(PersonaRepository repository) {
        super(repository);
    }
}

