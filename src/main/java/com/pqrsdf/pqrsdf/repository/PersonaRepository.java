package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Persona;
import java.util.Optional;


@Repository
public interface PersonaRepository extends GenericRepository<Persona, Long> {
    Optional<Persona> findByDni(String dni);
}