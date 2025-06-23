package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;
import com.pqrsdf.pqrsdf.service.ResponsablePQService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/responsables_pqs")
@Tag(name = "Gestion de Responsable de Pqs")
public class ResponsablePQController extends GenericController<ResponsablePQ, Long>{
    public ResponsablePQController(ResponsablePQService service){
        super(service);
    }
}
