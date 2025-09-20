package com.pqrsdf.pqrsdf.dto;

import java.sql.Time;
import java.util.Date;

import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.TipoPQ;

public interface InterfacePq {
    Long getId();

    String getConsecutivo();

    String getNumeroRadicado();

    Integer getNumeroFolio();

    String getDetalleAsunto();

    String getDetalleDescripcion();

    Date getFechaRadicacion();

    Time getHoraRadicacion();

    Date getFechaResolucionEstimada();

    Date getFechaResolucion();

    String getRespuesta();

    Boolean getWeb();

    Long getUltimoEstadoId();

    String getNombreUltimoEstado();

    // Relaciones que SÍ quieres incluir
    TipoPQ getTipoPQ();

    Persona getSolicitante();

}
