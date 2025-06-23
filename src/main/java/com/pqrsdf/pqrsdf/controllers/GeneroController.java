package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Genero;
import com.pqrsdf.pqrsdf.service.GeneroService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gestion de Generos")
public class GeneroController extends GenericController<Genero, Long> {
    public GeneroController(GeneroService service) {
        super(service);
    }
}
