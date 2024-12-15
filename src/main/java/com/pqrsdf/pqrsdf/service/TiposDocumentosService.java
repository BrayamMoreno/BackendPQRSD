package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.TiposDocumentos;
import com.pqrsdf.pqrsdf.repository.TiposDocumentosRepository;

@Service
public class TiposDocumentosService extends GenericService<TiposDocumentos, Long>{
    public TiposDocumentosService(TiposDocumentosRepository repository){
        super(repository);
    }
}
