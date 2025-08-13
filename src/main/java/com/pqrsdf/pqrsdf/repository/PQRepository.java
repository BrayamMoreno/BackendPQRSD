package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.PQ;
import java.util.List;
import java.time.LocalDate;



@Repository
public interface PQRepository extends GenericRepository<PQ, Long>{
    Page<PQ> findByResponsableId(Long responsableId, Pageable pageable);
    Page<PQ> findBySolicitanteId(Long solicitanteId, Pageable pageable);

    Page<PQ> findBySolicitanteIdOrderByFechaRadicacionDesc(Long solicitanteId, Pageable pageable);

    Page<PQ> findAllByOrderByFechaRadicacionDesc(Pageable pageable);

}
