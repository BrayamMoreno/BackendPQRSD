package com.pqrsdf.pqrsdf.dto;

import java.util.List;

public record ResolucionDto(
    Long pqId,
    Long responsableId,
    String comentario,
    List<String> listaCorreos,
    String asunto,
    String respuesta,
    List<DocumentoDTO> lista_documentos
    ){
}
