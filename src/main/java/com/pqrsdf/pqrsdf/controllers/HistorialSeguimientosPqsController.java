package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.HistorialSeguimientosPq;
import com.pqrsdf.pqrsdf.service.HistorialSeguimientosPqService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/historial_seguimientos_pq")
@Tag(name = "Gestion de Historial Seguimientos de Pqs")
public class HistorialSeguimientosPqsController extends GenericController<HistorialSeguimientosPq, Long>{
    public HistorialSeguimientosPqsController(HistorialSeguimientosPqService service){
        super(service);
    }
}
