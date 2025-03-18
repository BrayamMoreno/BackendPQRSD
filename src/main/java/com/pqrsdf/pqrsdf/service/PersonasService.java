package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;
import com.pqrsdf.pqrsdf.controllers.AdjuntosPqController;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.PersonasRepository;
import com.pqrsdf.pqrsdf.repository.UsuariosRepository;

@Service
public class PersonasService extends GenericService<Personas, Long>{

    private final AdjuntosPqController adjuntosPqController;

    private final PersonasRepository repository;
    private final UsuariosRepository usuariosRepository;

    public PersonasService(PersonasRepository repository, UsuariosRepository usuariosRepository, AdjuntosPqController adjuntosPqController) {
        super(repository);
        this.usuariosRepository = usuariosRepository;
        this.repository = repository;
        this.adjuntosPqController = adjuntosPqController;
    }
}

