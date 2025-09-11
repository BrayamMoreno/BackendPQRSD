package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.service.HistorialEstadoService;

import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping(path = "/api/historial_estados")
@Tag(name = "Historial de Estados de PQ")
public class HistorialEstadosController extends GenericController<HistorialEstadoPQ, Long> {

    public HistorialEstadosController(HistorialEstadoService service) {
        super(service);
    }

}
