package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.EstadosPq;
import com.pqrsdf.pqrsdf.service.EstadosPqService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/estados_pqs")
@Tag(name = "Gestion de Estados de Pqs")
public class EstadosPqController extends GenericController<EstadosPq, Long>{
    public EstadosPqController(EstadosPqService service){
        super(service);
    }
}
