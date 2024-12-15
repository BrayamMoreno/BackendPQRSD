package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.UsuariosRepository;

@Service
public class UsuariosService extends GenericService<Usuario, Long>{
    
    private final UsuariosRepository usuarioRepository;
    
    public UsuariosService(UsuariosRepository repository){
        super(repository);
        this.usuarioRepository = repository;
    }

    public Usuario getEntityByUsername(String Username){
        return usuarioRepository.findByUsername(Username).orElse(null);
    }
}
