package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.TipoPQ;
import com.pqrsdf.pqrsdf.service.TipoPQService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/tipos_pqs")
@Tag(name = "Gestion de Tipos de Pqs")
public class TipoPQController extends GenericController<TipoPQ, Long>{
    public TipoPQController(TipoPQService service){
        super(service);
    }
}
