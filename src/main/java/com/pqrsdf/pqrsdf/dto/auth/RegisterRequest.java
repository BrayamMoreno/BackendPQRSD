package com.pqrsdf.pqrsdf.dto.auth;

import java.util.Date;

public record RegisterRequest(

    String nombre,

    String apellido,

    Long tipoDocumento,

    String dni,

    Long tipoPersona,

    String telefono,

    Date fechaNacimiento,

    String direccion,

    Long municipioId,

    Long genero,

    String correo,

    String contraseña,

    boolean tratamientoDatos
) {}
