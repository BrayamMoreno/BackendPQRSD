package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.Specifications.PqsSpecification;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.dto.RadicarDto;
import com.pqrsdf.pqrsdf.dto.ResolucionDto;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.PersonaService;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.service.PQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PQController extends GenericController<PQ, Long> {

    private final PQService service;

    public PQController(PQService service, PersonaService personasService,
            AdjuntoPQService adjuntosPqService) {
        super(service);
        this.service = service;
    }

    @GetMapping("/all_pqs")
    public ResponseEntity<?> getAllPqs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = false) Long estadoId,
            @RequestParam(required = false) String numeroRadicado,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRadicacion").descending());

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasTipoId(tipoId))
                    .and(PqsSpecification.hasUltimoEstado(estadoId))
                    .and(PqsSpecification.hasNumeroRadicado(numeroRadicado))
                    .and(PqsSpecification.hasFechaRango(fechaInicio, fechaFin));

            if (service.findAll(pageable, spec).hasContent() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntityUtil
                    .handlePaginationRequest(service.findAll(pageable, spec));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/mis_pqs_usuarios")
    public ResponseEntity<?> getMyPqs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = true) Long solicitanteId,
            @RequestParam(required = false) Long estadoId,
            @RequestParam(required = false) String numeroRadicado,
            @RequestParam(required = false) String fechaRadicacion,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRadicacion").descending());

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasTipoId(tipoId))
                    .and(PqsSpecification.hasSolicitanteId(solicitanteId))
                    .and(PqsSpecification.hasUltimoEstado(estadoId))
                    .and(PqsSpecification.hasNumeroRadicado(numeroRadicado))
                    .and(PqsSpecification.hasFechaRango(fechaInicio, fechaFin));

            if (service.findAllPage(pageable, spec).hasContent() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntityUtil
                    .handlePaginationRequest(service.findAllPage(pageable, spec));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/sin_responsable")
    public ResponseEntity<?> getMyPqsByResponsable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = false) String numeroRadicado,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRadicacion").ascending()   );

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasTipoId(tipoId))
                    .and(PqsSpecification.hasResponsableNull())
                    .and(PqsSpecification.hasUltimoEstado(1L)) // Estado "Radicado"
                    .and(PqsSpecification.hasNumeroRadicado(numeroRadicado))
                    .and(PqsSpecification.hasFechaRango(fechaInicio, fechaFin));

            Page<PQ> pqs = service.findAll(pageable, spec);
            if (pqs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntityUtil.handlePaginationRequest(pqs);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/mis_pqs_contratistas")
    public ResponseEntity<?> getMyPqs(
            @RequestParam(required = true) Long responsableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = false) Long estadoId,
            @RequestParam(required = false) String numeroRadicado,
            @RequestParam(required = false) String fechaRadicacion) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaRadicacion").ascending());

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasTipoId(tipoId))
                    .and(PqsSpecification.hasUltimoEstado(estadoId))
                    .and(PqsSpecification.hasNumeroRadicado(numeroRadicado))
                    .and(PqsSpecification.hasResponsableId(responsableId));

            if (service.findAll(pageable, spec).hasContent() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntityUtil
                    .handlePaginationRequest(service.findAll(pageable, spec));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/vencidas")
    public ResponseEntity<?> getVencidas(
            @RequestParam(required = false) Long responsableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaResolucionEstimada").ascending());

            Long estadoId = 2L; // Estado "En Proceso"

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasResponsableId(responsableId))
                    .and(PqsSpecification.hasUltimoEstado(estadoId));

            Page<PQ> vencidas = service.findVencidas(pageable, spec);
            if (vencidas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntityUtil.handlePaginationRequest(vencidas);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/proximas_a_vencer")
    public ResponseEntity<?> getProximasAVencer(
            @RequestParam(required = false) Long responsableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaResolucionEstimada").ascending());

            Long estadoId = 2L;

            Specification<PQ> spec = Specification
                    .where(PqsSpecification.hasResponsableId(responsableId))
                    .and(PqsSpecification.hasUltimoEstado(estadoId));

            Page<PQ> proximasAVencer = service.findProximasAVencer(pageable, spec);
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

    @PatchMapping("/dar_resolucion")
    public ResponseEntity<?> darResolucion(@RequestBody ResolucionDto resolucionDto) {
        try {
            service.darResolucion(resolucionDto);
            return ResponseEntity.status(HttpStatus.OK).body("Resolución dada correctamente");
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/conteo-usuarios")
    public ResponseEntity<?> obtenerConteo(@PathParam("solicitanteId") Long solicitanteId) {
        try {
            if (solicitanteId == null) {
                return ResponseEntityUtil.handleBadRequest("El parámetro solicitanteId es obligatorio");
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.obtenerConteoPorSolicitante(solicitanteId));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/conteo-radicador")
    public ResponseEntity<?> obtenerConteoRadicador() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.conteoRadicador());
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
