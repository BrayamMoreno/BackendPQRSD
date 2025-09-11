package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Municipio;
import com.pqrsdf.pqrsdf.service.MunicipioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/municipios")
@Tag(name = "Gestion de Municipios")
public class MunicipioController extends GenericController<Municipio, Long> {

    private final MunicipioService service;

    public MunicipioController(MunicipioService service) {
        super(service);
        this.service = service;
    }

}
