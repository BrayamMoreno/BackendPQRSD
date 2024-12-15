package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.DepartamentosResp;
import com.pqrsdf.pqrsdf.service.DepartamentosRespService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/departamentos_responsables")
@Tag(name = "Gestion de Departamentos Responsables")
public class DepartamentosRespController extends GenericController<DepartamentosResp, Long>{
    public DepartamentosRespController(DepartamentosRespService service){
        super(service);
    }
}
