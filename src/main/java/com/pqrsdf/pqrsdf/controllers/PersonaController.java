package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.service.PersonaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/personas")
@Tag(name = "Gestion de Personas")
public class PersonaController extends GenericController<Persona, Long> {
    public PersonaController(PersonaService service){
        super(service);
    }
}
