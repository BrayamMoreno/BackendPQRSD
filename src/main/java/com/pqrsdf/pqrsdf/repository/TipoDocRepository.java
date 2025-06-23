package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.TipoDoc;

@Repository
public interface TipoDocRepository extends GenericRepository<TipoDoc, Long>{
    
}
