package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.service.AdjuntosPqService;
import com.pqrsdf.pqrsdf.service.PersonasService;
import com.pqrsdf.pqrsdf.service.PqsService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PqsController extends GenericController<Pqs, Long>{
    
    private final PqsService service;
    private final PersonasService personasService;
    private final AdjuntosPqService adjuntosPqService;
    
    public PqsController(PqsService service, PersonasService personasService,
                        AdjuntosPqService adjuntosPqService) {
        super(service);
        this.service = service;
        this.personasService = personasService;
        this.adjuntosPqService = adjuntosPqService;
    }

    @PostMapping("/radicar_pq")
    public ResponseEntity<?> createPqEntity(@RequestBody String entity, @RequestParam Long personaId) {
        try {
            return null;
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/no_asignados")
    public ResponseEntity<?> getPqsNoAssigned() {
        try {
            if (service.findByResponsableId(null).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.findByResponsableId(null));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/asignados")
    public ResponseEntity<?> getPqsAssigned(@RequestParam Long responsableId) {
        try {
            if (service.findByResponsableId(responsableId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.findByResponsableId(responsableId));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
