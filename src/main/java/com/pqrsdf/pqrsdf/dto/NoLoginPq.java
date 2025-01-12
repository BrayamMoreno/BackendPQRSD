package com.pqrsdf.pqrsdf.dto;

import java.util.List;

public record NoLoginPq(
    Long tipo_pq_id,
    Long tipo_doc_id,
    String dni,
    Long tipo_persona_id,
    Long genero,
    Long municipio_id,
    String nombres,
    String apellidos,
    String celular,
    String asunto,
    String correo,
    String mensaje,
    //ttd = tratamiento de datos
    boolean tratamiento_datos,
    List<String> adjuntos
){}
