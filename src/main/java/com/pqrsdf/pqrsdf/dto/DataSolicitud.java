package com.pqrsdf.pqrsdf.dto;

import lombok.Builder;

@Builder
public record DataSolicitud(
    String nombre,
    String apellido,
    String correo,
    String telefono,
    String direccion
){
}