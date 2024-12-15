package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.TiposDocumentos;
import com.pqrsdf.pqrsdf.service.TiposDocumentosService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/tipos_documentos")
@Tag(name = "Gestion de Tipos de Documentos")
public class TiposDocumentosController extends GenericController<TiposDocumentos, Long>{
    public TiposDocumentosController(TiposDocumentosService service){
        super(service);
    }
}
