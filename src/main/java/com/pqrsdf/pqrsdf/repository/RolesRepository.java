package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Roles;

@Repository
public interface RolesRepository extends GenericRepository<Roles, Long> {
    
}
