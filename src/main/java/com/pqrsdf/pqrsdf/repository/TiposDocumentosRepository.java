package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.TiposDocumentos;

@Repository
public interface TiposDocumentosRepository extends GenericRepository<TiposDocumentos, Long>{
    
}
