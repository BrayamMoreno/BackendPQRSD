package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.RadicadorPq;
import com.pqrsdf.pqrsdf.service.RadicadorPqService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/radicador_pq")
@Tag(name = "Gestion de Radicadores de Pqs")
public class RadicadorPqController extends GenericController<RadicadorPq, Long>{
    public RadicadorPqController(RadicadorPqService service){
        super(service);
    }
}
