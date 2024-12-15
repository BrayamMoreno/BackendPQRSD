package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Departamentos;
import com.pqrsdf.pqrsdf.service.DepartamenttosService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(path = "/api/departamentos")
@Tag(name = "Gestion de Departamentos")
public class DepatamentosController extends GenericController<Departamentos, Long>{
    public DepatamentosController(DepartamenttosService service){
        super(service);
    }
}
