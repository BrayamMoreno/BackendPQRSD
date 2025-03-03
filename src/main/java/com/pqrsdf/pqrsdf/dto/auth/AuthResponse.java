package com.pqrsdf.pqrsdf.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt","logged"})
public record AuthResponse(
    String username,
    String message,
    String jwt,
    boolean logged
){}