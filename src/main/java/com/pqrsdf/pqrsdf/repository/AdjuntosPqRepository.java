package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.AdjuntosPq;
import java.util.List;

@Repository
public interface AdjuntosPqRepository extends GenericRepository <AdjuntosPq, Long>{
    List<AdjuntosPq> findByPqId(Long pqId);
}
