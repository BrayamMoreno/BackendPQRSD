package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.EstadoPQ;
import com.pqrsdf.pqrsdf.service.EstadoPQService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/estados_pqs")
@Tag(name = "Gestion de Estados de Pqs")
public class EstadoPQController extends GenericController<EstadoPQ, Long>{
    public EstadoPQController(EstadoPQService service){
        super(service);
    }
}
