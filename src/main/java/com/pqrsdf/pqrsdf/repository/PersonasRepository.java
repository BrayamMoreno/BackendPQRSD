package com.pqrsdf.pqrsdf.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Personas;

@Repository
public interface PersonasRepository extends GenericRepository<Personas, Long> {
   Optional<Personas> findByTipoDocAndDni(Long tipoDoc, String dni);
}

