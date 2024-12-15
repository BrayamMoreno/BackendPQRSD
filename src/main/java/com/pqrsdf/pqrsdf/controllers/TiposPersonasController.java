package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.TiposPersonas;
import com.pqrsdf.pqrsdf.service.TiposPersonasService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/tipos_personas")
@Tag(name = "Gestion de Tipos de Personas")
public class TiposPersonasController extends GenericController<TiposPersonas, Long>{
    public TiposPersonasController(TiposPersonasService service){
        super(service);
    }
}
