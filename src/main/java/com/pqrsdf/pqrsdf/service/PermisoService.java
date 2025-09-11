package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Permiso;
import com.pqrsdf.pqrsdf.repository.PermisoRepository;

@Service

public class PermisoService extends GenericService<Permiso, Long>{
    public PermisoService(PermisoRepository repository){
        super(repository);
    }
}
