package com.pqrsdf.pqrsdf.dto;

public record HistorialEstadoRequest(
    String numeroRadicado,
    Estado estado,
    String observacion,
    String fechaCambio,
    Usuario usuario
) {
    public record Estado(
        Long id
    ){}

    public record Usuario(
        Long id
    ) {}
}