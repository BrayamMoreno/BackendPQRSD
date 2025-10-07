package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import java.util.List;

@Repository
public interface AdjuntoPQRepository extends GenericRepository <AdjuntoPQ, Long>, JpaSpecificationExecutor<AdjuntoPQ> {
    List<AdjuntoPQ> findByPqId(Long pqId);
}
