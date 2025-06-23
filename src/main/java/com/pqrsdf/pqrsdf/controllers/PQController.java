package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.service.PersonaService;
import com.pqrsdf.pqrsdf.service.PQService;
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
public class PQController extends GenericController<PQ, Long>{
    
    private final PQService service;
    private final PersonaService personasService;
    private final AdjuntoPQService adjuntosPqService;
    
    public PQController(PQService service, PersonaService personasService,
                        AdjuntoPQService adjuntosPqService) {
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
