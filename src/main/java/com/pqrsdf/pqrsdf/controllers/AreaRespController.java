package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.AreaResp;
import com.pqrsdf.pqrsdf.service.AreaRespService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/areas_responsables")
@Tag(name = "Gestion de Areas Responsables")
public class AreaRespController extends GenericController<AreaResp, Long>{
    public AreaRespController(AreaRespService service){
        super(service);
    }
}
