package com.pqrsdf.pqrsdf.dto;

public record UpdateAdjuntoRequest(
    Long adjuntoPqId,
    String nombreArchivo,
    Long pqId,
    boolean esRespuesta
){
}
