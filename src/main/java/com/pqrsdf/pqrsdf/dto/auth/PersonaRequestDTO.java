package com.pqrsdf.pqrsdf.dto.auth;

public record PersonaRequestDTO(
        String nombre,
        String apellido,
        TipoDocDTO tipoDoc,
        String dni,
        TipoPersonaDTO tipoPersona,
        String telefono,
        String direccion,
        Boolean tratamientoDatos,
        MunicipioDTO municipio,
        GeneroDTO genero,
        String correoUsuario
) {
    public record TipoDocDTO(Long id) {}
    public record TipoPersonaDTO(Long id) {}
    public record MunicipioDTO(Long id) {}
    public record GeneroDTO(Long id) {}
}
