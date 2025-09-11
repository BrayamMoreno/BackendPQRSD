package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Rol;
import com.pqrsdf.pqrsdf.service.RolService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/roles")
@Tag(name = "Gestion de Roles")
public class RolController extends GenericController<Rol, Long>{
    public RolController(RolService service){
        super(service);
    }
}
