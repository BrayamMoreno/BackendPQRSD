package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;

@Repository
public interface ResponsablePQRepository extends GenericRepository<ResponsablePQ, Long>{
    
}
