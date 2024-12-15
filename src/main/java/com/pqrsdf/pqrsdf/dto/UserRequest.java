package com.pqrsdf.pqrsdf.dto;

public record UserRequest(
    String username,
    String password,
    long personaId,
    long rolId
    ){
}
