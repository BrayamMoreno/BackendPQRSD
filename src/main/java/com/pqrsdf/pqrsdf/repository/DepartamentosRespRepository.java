package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.AreasResp;

@Repository
public interface DepartamentosRespRepository extends GenericRepository<AreasResp, Long>{
    
}
