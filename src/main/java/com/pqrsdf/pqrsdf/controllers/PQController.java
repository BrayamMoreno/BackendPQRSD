package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.dto.RadicarDto;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.PersonaService;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.service.PQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @Override
    @GetMapping()
    public ResponseEntity<?> getAllEntities(
            @RequestParam(required = false, defaultValue = "id") String order_by,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order_by));
            return ResponseEntityUtil.handlePaginationRequest(service.findByFechaRadicacionDes(pageable));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/proximas_a_vencer")
    public ResponseEntity<?> getProximasAVencer(
            @RequestParam(required = false, defaultValue = "id") String order_by,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaResolucionEstimada").ascending());
            Page<PQ> proximasAVencer = service.findProximasAVencer(pageable);
            if (proximasAVencer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntityUtil.handlePaginationRequest(proximasAVencer);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/ultimos_7_dias")
    public ResponseEntity<?> getUltimos7Dias() {
        try {
            List<Map<String, Object>> ultimos7Dias = service.obtenerTendenciasDiarias();
            if (ultimos7Dias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ultimos7Dias);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/contar_por_tipo_mes")
    public ResponseEntity<?> contarPorTipoMes() {
        try {
            List<Map<String, Object>> conteoPorTipo = service.obtenerConteoPorTipoMes();
            if (conteoPorTipo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(conteoPorTipo);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/sin_responsable")
    public ResponseEntity<?> getMyPqsByResponsable(
            @RequestParam(required = false, defaultValue = "id") String order_by,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order_by));
            Page<PQ> pqs = service.findByResponsableIdOrderByFechaRadicacionDesc(pageable);
            if (pqs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntityUtil.handlePaginationRequest(pqs);
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

    @PostMapping("/aprobacion_pq")
    public ResponseEntity<?> aprobacionPq(@RequestBody RadicarDto entity) {
        try {
            service.aceptarRechazarPq(entity);
            return ResponseEntity.status(HttpStatus.OK).body("PQ aceptada correctamente");
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
