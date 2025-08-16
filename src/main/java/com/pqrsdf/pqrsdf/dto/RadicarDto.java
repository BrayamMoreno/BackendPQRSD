package com.pqrsdf.pqrsdf.dto;

public record RadicarDto(
    Long radicadorId,
    Long solicitudId,
    String fechaResolucionEstimada,
    long responsableId,
    String comentario,
    boolean isAprobada
    ){
}
