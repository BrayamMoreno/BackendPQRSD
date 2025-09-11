package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Permiso;

@Repository
public interface PermisoRepository extends GenericRepository<Permiso, Long>{

}
