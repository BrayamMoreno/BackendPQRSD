package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.HistorialEstadosPq;
import com.pqrsdf.pqrsdf.service.HistorialEstadosPqService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/historial_estados_pqs")
@Tag(name = "Gestion de Historial Estados de Pqs")
public class HistorialEstadosPqsController extends GenericController<HistorialEstadosPq, Long>{
    public HistorialEstadosPqsController(HistorialEstadosPqService service){
        super(service);
    }
}