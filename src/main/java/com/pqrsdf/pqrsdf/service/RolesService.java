package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Roles;
import com.pqrsdf.pqrsdf.repository.RolesRepository;

@Service
public class RolesService extends GenericService<Roles, Long> {
    public RolesService(RolesRepository repository){
        super(repository);
    }
}
