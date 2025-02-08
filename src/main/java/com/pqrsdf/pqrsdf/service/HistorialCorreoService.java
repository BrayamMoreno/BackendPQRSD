package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialCorreos;
import com.pqrsdf.pqrsdf.repository.HistorialCorreoRepository;

@Service
public class HistorialCorreoService extends GenericService<HistorialCorreos, Long> {
    public HistorialCorreoService(HistorialCorreoRepository repository){
        super(repository);
    }
}
