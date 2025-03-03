package com.pqrsdf.pqrsdf.dto;

public record UserRequest(
    String correo,
    String contraseña,
    long personaId,
    long rolId
){}
