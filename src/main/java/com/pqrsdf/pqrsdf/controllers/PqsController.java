package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.service.PqsService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PqsController extends GenericController<Pqs, Long>{
    public PqsController(PqsService service){
        super(service);
    }
}
