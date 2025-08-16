package com.pqrsdf.pqrsdf.dto;

public record RadicarDto(
    Long RadicadorId,
    Long solicitudId,
    String fechaResolucionEstimada,
    long responsableId,
    String comentario,
    boolean isAprobada
    ){
}
