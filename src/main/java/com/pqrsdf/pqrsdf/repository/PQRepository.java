package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.PQ;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PQRepository extends GenericRepository<PQ, Long> {

    Page<PQ> findByResponsableId(Long responsableId, Pageable pageable);

    Page<PQ> findBySolicitanteId(Long solicitanteId, Pageable pageable);

    Page<PQ> findBySolicitanteIdOrderByFechaRadicacionDesc(Long solicitanteId, Pageable pageable);

    Page<PQ> findAllByOrderByFechaRadicacionDesc(Pageable pageable);

    Page<PQ> findByFechaResolucionEstimadaBetween(LocalDate hoy, LocalDate limite, Pageable pageable);

    @Query("SELECT p FROM PQ p WHERE p.fechaRadicacion >= :fechaInicio")
    List<PQ> findUltimos7Dias(@Param("fechaInicio") LocalDate fechaInicio);

}
