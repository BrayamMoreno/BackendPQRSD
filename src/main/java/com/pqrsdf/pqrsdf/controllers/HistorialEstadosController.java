package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.service.HistorialEstadoService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/api/historial_estados")
@Tag(name = "Historial de Estados de PQ")
public class HistorialEstadosController extends GenericController<HistorialEstadoPQ, Long> {

    private final HistorialEstadoService service;

    public HistorialEstadosController(HistorialEstadoService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/GetByPqId")
    public ResponseEntity<?> getByPqId(@RequestParam Long pqId) {
        try{
            if(pqId == null) {
                return ResponseEntityUtil.handleBadRequest("El id no puede ser nulo"); // Retorna una lista vacía si pqId es nulo
            }
            if(service.findByPqId(pqId).isEmpty()) {
                return ResponseEntityUtil.handleBadRequest("No hay estados para esta petición"); // Retorna una lista vacía si no se encuentran registros
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.findByPqId(pqId));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
