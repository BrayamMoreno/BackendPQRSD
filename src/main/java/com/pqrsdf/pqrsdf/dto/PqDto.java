package com.pqrsdf.pqrsdf.dto;

import java.util.List;

public record PqDto(
    String tipo_pq_id,
    String solicitante_id,
    String detalleAsunto,
    String detalleDescripcion,
    List<DocumentoDTO> lista_documentos
){}
