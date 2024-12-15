package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.TiposPqs;
import com.pqrsdf.pqrsdf.service.TiposPqsService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/tipos_pqs")
@Tag(name = "Gestion de Tipos de Pqs")
public class TiposPqsController extends GenericController<TiposPqs, Long>{
    public TiposPqsController(TiposPqsService service){
        super(service);
    }
}
