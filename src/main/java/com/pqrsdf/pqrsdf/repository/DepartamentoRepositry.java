package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Departamento;

@Repository
public interface DepartamentoRepositry extends GenericRepository<Departamento, Long>{

    
}
