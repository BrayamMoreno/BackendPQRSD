package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Permiso;
import com.pqrsdf.pqrsdf.service.PermisoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/permisos")
@Tag(name = "Gestion de Permisos")
public class PermisoController extends GenericController<Permiso, Long> {
    public PermisoController(PermisoService service) {
        super(service);
    }
}
