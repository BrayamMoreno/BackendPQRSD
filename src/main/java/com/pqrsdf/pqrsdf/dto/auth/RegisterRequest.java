package com.pqrsdf.pqrsdf.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotBlank(message = "El apellido es obligatorio")
    String apellido,

    @NotNull(message = "El tipo de documento es obligatorio")
    Long tipoDocumento,

    @NotBlank(message = "El documento es obligatorio")
    String dni,

    @NotNull(message = "El tipo de persona es obligatorio")
    Long tipoPersona,

    @NotBlank(message = "El teléfono es obligatorio")
    String telefono,

    String direccion,

    @NotNull(message = "El municipio es obligatorio")
    Long municipioId,

    @NotNull(message = "El género es obligatorio")
    Long genero,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un correo válido")
    String correo,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 6 caracteres")
    String contraseña

) {}
