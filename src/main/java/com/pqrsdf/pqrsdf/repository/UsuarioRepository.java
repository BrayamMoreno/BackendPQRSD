package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Usuario;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends GenericRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String Correo);

    Page<Usuario> findByRolId(long Id, Pageable pageable);
}
