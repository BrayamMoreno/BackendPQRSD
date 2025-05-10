package com.pqrsdf.pqrsdf.repository;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Pqs;
import java.util.List;


@Repository
public interface PqsRepository extends GenericRepository<Pqs, Long>{
    List<Pqs> findByResponsableId(Long responsableId);
    List<Pqs> findBySolicitanteId(Long solicitanteId);
}
