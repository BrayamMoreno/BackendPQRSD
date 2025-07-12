package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.PersonaService;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.service.PQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PQController extends GenericController<PQ, Long> {

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

    @GetMapping("/mis_pqs")
    public ResponseEntity<?> getMyPqs(@RequestParam Long solicitanteId,
        @RequestParam(required = false, defaultValue = "id") String order_by,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order_by));
            if (solicitanteId == null) {
                return ResponseEntityUtil.handleBadRequest(
                        "El ID del responsable no puede ser nulo");
            }
            if (service.findBySolicitanteId(solicitanteId, pageable).hasContent() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntityUtil.handlePaginationRequest(service.findBySolicitanteId(solicitanteId, pageable));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping("/radicar_pq")
    public ResponseEntity<?> radicarPq(@RequestBody PqDto data) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createPq(data));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
