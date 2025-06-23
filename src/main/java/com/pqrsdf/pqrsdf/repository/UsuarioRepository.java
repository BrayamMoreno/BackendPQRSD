package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Usuario;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends GenericRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String Correo);
}
