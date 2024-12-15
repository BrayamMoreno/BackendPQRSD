package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Pqs;

@Repository
public interface PqsRepository extends GenericRepository<Pqs, Long>{
    
}
