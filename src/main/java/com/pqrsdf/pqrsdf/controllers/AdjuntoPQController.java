package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/api/adjuntosPq")
@Tag(name = "Gestion de Adjuntos de PQ")
public class AdjuntoPQController extends GenericController<AdjuntoPQ, Long> {

    private final AdjuntoPQService service;

    public AdjuntoPQController(AdjuntoPQService service) {
        super(service);
        this.service = service;
    }
    
    @GetMapping("/getByPqId")
    public ResponseEntity<?> getMethodName(@RequestParam Long pqId) {
        try {
            if(pqId == null){
                return ResponseEntityUtil.handleBadRequest("El id de la PQ es requerido");
            }

            if(pqId <= 0){
                return ResponseEntityUtil.handleBadRequest("El id de la PQ no puede ser menor o igual a 0");
            }

            return ResponseEntity.status(HttpStatus.OK).body(service.findByPqId(pqId));

        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
