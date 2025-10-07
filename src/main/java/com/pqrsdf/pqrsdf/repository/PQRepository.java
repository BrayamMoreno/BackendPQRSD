package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.dto.ConteoPQDTO;
import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.PQ;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PQRepository extends GenericRepository<PQ, Long>, JpaSpecificationExecutor<PQ> {

    @Query("SELECT p FROM PQ p WHERE p.fechaRadicacion >= :fechaInicio")
    List<PQ> findUltimos7Dias(@Param("fechaInicio") Date fechaInicio);

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

    @Query(value = """
                SELECT
                    COALESCE(SUM(CASE WHEN he_last.estado_id = 4 THEN 1 ELSE 0 END), 0) AS resueltas,
                    COALESCE(SUM(CASE WHEN he_last.estado_id = 3 THEN 1 ELSE 0 END), 0) AS rechazadas,
                    COALESCE(SUM(CASE WHEN he_last.estado_id IN (1,2) THEN 1 ELSE 0 END), 0) AS pendientes
                FROM pqs p
                LEFT JOIN LATERAL (
                    SELECT he.estado_id
                    FROM historial_estados_pq he
                    WHERE he.pq_id = p.id
                    ORDER BY he.fecha_cambio DESC
                    LIMIT 1
                ) he_last ON true
                WHERE p.solicitante_id = :solicitanteId
            """, nativeQuery = true)
    Object[] obtenerResumenEstadosPorSolicitante(@Param("solicitanteId") Long solicitanteId);

    @Query(value = "SELECT * FROM vista_conteo_pq WHERE solicitante_id = :solicitanteId", nativeQuery = true)
    ConteoPQDTO contarPorSolicitante(@Param("solicitanteId") Long solicitanteId);

}
