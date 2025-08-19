package com.pqrsdf.pqrsdf.dto;

public record DocumentoDTO(
    String Nombre,
    String Tipo,
    String Contenido,
    boolean isRespuesta
){
}