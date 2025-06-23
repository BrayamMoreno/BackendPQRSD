package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;
import com.pqrsdf.pqrsdf.controllers.AdjuntoPQController;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.PersonaRepository;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

@Service
public class PersonaService extends GenericService<Persona, Long>{

    private final AdjuntoPQController adjuntosPqController;

    private final PersonaRepository repository;
    private final UsuarioRepository usuariosRepository;

    public PersonaService(PersonaRepository repository, UsuarioRepository usuariosRepository, AdjuntoPQController adjuntosPqController) {
        super(repository);
        this.usuariosRepository = usuariosRepository;
        this.repository = repository;
        this.adjuntosPqController = adjuntosPqController;
    }
}

