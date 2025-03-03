package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.TiposDoc;

@Repository
public interface TiposDocumentosRepository extends GenericRepository<TiposDoc, Long>{
    
}
