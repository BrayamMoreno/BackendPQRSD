package com.pqrsdf.pqrsdf.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Usuario;

@JsonPropertyOrder({"username","message","jwt","userId","logged"})
public record AuthResponse(
    String username,
    String message,
    String jwt,
    Usuario usuario,
    Persona persona,
    boolean logged
){}