package com.pqrsdf.pqrsdf.dto;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.models.TipoPQ;

public record PqResponseDto(
        Long id,
        String numeroRadicado,
        String detalleAsunto,
        String detalleDescripcion,
        Date fechaRadicacion,
        Time horaRadicacion,
        Date fechaResolucionEstimada,
        Date fechaResolucion,
        String respuesta,
        Boolean web,
        Long ultimoEstadoId,
        String nombreUltimoEstado,
        TipoPQ tipoPQ,
        Set<AdjuntoPQ> adjuntos) {
}
