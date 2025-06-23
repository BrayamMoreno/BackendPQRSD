package com.pqrsdf.pqrsdf.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.service.PersonaService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/personas")
@Tag(name = "Gestion de Personas")
public class PersonaController extends GenericController<Persona, Long> {

    private final PersonaService service;

    public PersonaController(PersonaService service){
        super(service);
        this.service = service;
    }
    
}
