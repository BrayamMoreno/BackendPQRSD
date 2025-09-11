package com.pqrsdf.pqrsdf.dto;

public record RadicarDto(
    Long radicadorId,
    Long solicitudId,
    long responsableId,
    String comentario,
    String motivoRechazo,
    boolean isAprobada
    ){
}
