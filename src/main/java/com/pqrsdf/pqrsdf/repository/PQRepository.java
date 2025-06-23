package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.PQ;
import java.util.List;


@Repository
public interface PQRepository extends GenericRepository<PQ, Long>{
    List<PQ> findByResponsableId(Long responsableId);
    List<PQ> findBySolicitanteId(Long solicitanteId);
}
