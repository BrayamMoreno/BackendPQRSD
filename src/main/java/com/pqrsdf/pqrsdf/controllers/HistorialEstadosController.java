package com.pqrsdf.pqrsdf.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.HistorialEstadoRequest;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.HistorialEstadoService;
import com.pqrsdf.pqrsdf.service.PQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/historial_estados")
@Tag(name = "Historial de Estados de PQ")
public class HistorialEstadosController extends GenericController<HistorialEstadoPQ, Long> {

    private final HistorialEstadoService historialEstadoService;
    private final PQService pqService;

    public HistorialEstadosController(HistorialEstadoService historialEstadoService, PQService pqService) {
        super(historialEstadoService);
        this.historialEstadoService = historialEstadoService;
        this.pqService = pqService;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> actualizarEstados(@PathVariable Long id, @RequestBody HistorialEstadoRequest request) {
        try {
            Optional<PQ> pq = pqService.getByNumeroRadicado(request.numeroRadicado());
            if (pq.isPresent()) {
                historialEstadoService.actualizarHistorial(request, pq.get());
                return ResponseEntity.ok("Historial de estado actualizado correctamente.");
            } else {
                return ResponseEntityUtil.handleNotFoundError("not found pq");
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearHistorialEstado(@RequestBody HistorialEstadoRequest request) {
        try {
            Optional<PQ> pq = pqService.getByNumeroRadicado(request.numeroRadicado());
            if (pq.isPresent()) {
                historialEstadoService.crearHistorial(request, pq.get());
                return ResponseEntity.ok("Historial de estado creado correctamente.");
            } else {
                return ResponseEntityUtil.handleNotFoundError("not found pq");
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}


