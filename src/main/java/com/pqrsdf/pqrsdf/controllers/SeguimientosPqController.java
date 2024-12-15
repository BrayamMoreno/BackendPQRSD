package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.SeguimientosPq;
import com.pqrsdf.pqrsdf.service.SeguimientosPqService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/seguimientos_pqs")
@Tag(name = "Gestion de Seguimientos de Pqs")
public class SeguimientosPqController extends GenericController<SeguimientosPq, Long>{
    public SeguimientosPqController(SeguimientosPqService service){
        super(service);
    }
}
