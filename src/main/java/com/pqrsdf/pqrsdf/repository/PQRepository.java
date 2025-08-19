package com.pqrsdf.pqrsdf.repository;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
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

    Page<PQ> findByResponsableIdOrderByFechaRadicacionDesc(Long responsableId, Pageable pageable);

    Page<PQ> findBySolicitanteIdOrderByFechaRadicacionDesc(Long solicitanteId, Pageable pageable);

    Page<PQ> findAllByOrderByFechaRadicacionDesc(Pageable pageable);

    Page<PQ> findByFechaResolucionEstimadaBetween(LocalDate hoy, LocalDate limite, Pageable pageable);

    @Query("SELECT p FROM PQ p WHERE p.fechaRadicacion >= :fechaInicio")
    List<PQ> findUltimos7Dias(@Param("fechaInicio") LocalDate fechaInicio);

    @Query(value = """
                SELECT
                    t.nombre AS tipo,
                    SUM(
                        CASE
                            WHEN p.fecha_radicacion BETWEEN :inicioMes AND :finMes
                            THEN 1
                            ELSE 0
                        END
                    ) AS cantidad
                FROM tipos_pq t
                LEFT JOIN pqs p
                    ON p.tipo_pq_id = t.id
                GROUP BY t.nombre
                ORDER BY t.nombre
            """, nativeQuery = true)
    List<Object[]> contarPorTipoEnMes(
            @Param("inicioMes") LocalDate inicioMes,
            @Param("finMes") LocalDate finMes);

    @Query("SELECT p FROM PQ p WHERE p.ultimoEstadoId = 1 AND p.responsable IS NULL ORDER BY p.fechaRadicacion ASC")
    Page<PQ> findPendientesSinResponsable(Pageable pageable);

    @Query("SELECT p FROM PQ p WHERE p.responsable.id = :responsableId AND p.ultimoEstadoId = :estadoId")
    Page<PQ> findByResponsableAndEstado(@Param("responsableId") Long responsableId, @Param("estadoId") Long estadoId, Pageable pageable);

    @Query("SELECT p FROM PQ p WHERE p.ultimoEstadoId = 1 AND p.responsable.id = :responsableId ORDER BY p.fechaRadicacion ASC")
    List<PQ> findByResponsableAndEstadoList(@Param("responsableId") Long responsableId);

}
