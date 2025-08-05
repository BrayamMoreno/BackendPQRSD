package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Genero;

@Repository
public interface GeneroRepository extends GenericRepository<Genero, Long>{
    
}
