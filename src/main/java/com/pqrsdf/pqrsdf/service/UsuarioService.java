package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

@Service
public class UsuarioService extends GenericService<Usuario,Long> {
    
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService(UsuarioRepository repository){
        super(repository);
        this.usuarioRepository = repository;
    }

    public Usuario getEntityByUsername(String Username){
        return usuarioRepository.findByUsername(Username).orElse(null);
    }
}
