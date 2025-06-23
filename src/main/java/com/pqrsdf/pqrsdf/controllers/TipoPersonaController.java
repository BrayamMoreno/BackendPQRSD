package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.TipoPersona;
import com.pqrsdf.pqrsdf.service.TipoPersonaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/tipos_personas")
@Tag(name = "Gestion de Tipos de Personas")
public class TipoPersonaController extends GenericController<TipoPersona, Long>{
    public TipoPersonaController(TipoPersonaService service){
        super(service);
    }
}
