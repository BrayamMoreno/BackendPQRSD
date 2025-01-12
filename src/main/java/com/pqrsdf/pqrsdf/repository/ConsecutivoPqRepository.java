package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.ConsecutivoPq;

@Repository
public interface ConsecutivoPqRepository extends GenericRepository<ConsecutivoPq, Long> {
    
    <Optional>ConsecutivoPq findByYear(int year);
    
}
