package com.pqrsdf.pqrsdf.dto;

import java.util.List;

public record AdjuntoRequest(
    Long pqId,
    List<DocumentoDTO> lista_documentos
){
}