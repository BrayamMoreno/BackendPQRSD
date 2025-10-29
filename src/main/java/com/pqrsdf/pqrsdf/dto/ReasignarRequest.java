package com.pqrsdf.pqrsdf.dto;

public record ReasignarRequest(
    Long pqId,
    Long nuevoResponsableId,
    Long cambiadoPorId,
    String comentario) {
}
