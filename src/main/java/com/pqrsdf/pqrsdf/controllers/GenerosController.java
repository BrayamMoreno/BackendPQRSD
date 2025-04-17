package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Generos;
import com.pqrsdf.pqrsdf.service.GenerosService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gestion de Generos")
public class GenerosController extends GenericController<Generos, Long> {
    public GenerosController(GenerosService service) {
        super(service);
    }
}
