package com.pqrsdf.pqrsdf.dto;

import java.util.List;
import java.util.Map;

public record NoLoginPq(
    Long tipo_pq_id,
    Long tipo_doc_id,
    String dni,
    Long tipo_persona_id,
    Long genero,
    Long municipio_id,
    String nombres,
    String apellidos,
    String telefono,
    String asunto,
    String correo,
    String direccion,
    String mensaje,
    //ttd = tratamiento de datos
    boolean tratamiento_datos,
    List<Map<String, String>> Adjuntos

){}