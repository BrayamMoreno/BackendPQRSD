package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Departamentos;

@Repository
public interface DepartamentosRepositry extends GenericRepository<Departamentos, Long>{

    
}
