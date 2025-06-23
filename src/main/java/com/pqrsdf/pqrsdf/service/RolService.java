package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Rol;
import com.pqrsdf.pqrsdf.repository.RolRepository;

@Service
public class RolService extends GenericService<Rol, Long> {
    public RolService(RolRepository repository){
        super(repository);
    }
}
