package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialTelefonos;
import com.pqrsdf.pqrsdf.repository.HistorialTelefonosRepository;

@Service
public class HistorialTelefonoService extends GenericService<HistorialTelefonos, Long> {
    
    public HistorialTelefonoService(HistorialTelefonosRepository repository){
        super(repository);
    }
    
}
