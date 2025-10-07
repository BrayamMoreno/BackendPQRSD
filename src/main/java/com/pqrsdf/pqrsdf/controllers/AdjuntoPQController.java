package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.Specifications.AdjuntosSpecification;
import com.pqrsdf.pqrsdf.dto.AdjuntoRequest;
import com.pqrsdf.pqrsdf.dto.UpdateAdjuntoRequest;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.service.PQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "/api/adjuntos_pq")
@Tag(name = "Gestion de Adjuntos de PQ")
public class AdjuntoPQController extends GenericController<AdjuntoPQ, Long> {

    private final AdjuntoPQService service;
    private final PQService pqService;

    public AdjuntoPQController(AdjuntoPQService service, PQService pqService) {
        super(service);
        this.service = service;
        this.pqService = pqService;
    }

    @GetMapping("/GetAdjuntosPq")
    public ResponseEntity<?> getAdjuntosPq(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean respuesta,
            @RequestParam(required = false) String nombreRadicado) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

            Specification<AdjuntoPQ> spec = Specification
                    .where(AdjuntosSpecification.hasRespuesta(respuesta))
                    .and(AdjuntosSpecification.hasNombreArchivoOrPqRadicado(nombreRadicado));

            return ResponseEntityUtil.handlePaginationRequest(service.findAll(pageable, spec));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/GetByPqId")
    public ResponseEntity<?> getMethodName(@RequestParam Long pqId) {
        try {
            if (pqId == null) {
                return ResponseEntityUtil.handleBadRequest("El id de la PQ es requerido");
            }
            if (pqId <= 0) {
                return ResponseEntityUtil.handleBadRequest("El id de la PQ no puede ser menor o igual a 0");
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.findByPqId(pqId));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            AdjuntoPQ adj = service.getById(id);

            Path filePath = Path.of(adj.getRutaArchivo());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Archivo no encontrado en disco");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + adj.getNombreArchivo() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("crear_adjunto")
    public ResponseEntity<?> createAdjunto(@RequestBody AdjuntoRequest request) {
        try {
            // Validate request
            if (request.pqId() == null || request.pqId() <= 0) {
                return ResponseEntityUtil.handleBadRequest("El id de la PQ es requerido y debe ser mayor que 0");
            }
            if (request.lista_documentos() == null || request.lista_documentos().isEmpty()) {
                return ResponseEntityUtil.handleBadRequest("La lista de documentos es requerida");
            }

            PQ pq = pqService.getById(request.pqId());

            if (pq == null) {
                return ResponseEntityUtil.handleNotFoundError("La petcion con el numero de radicdo " + request.pqId() + " no fue encontrada");
            }

            service.createAdjuntosPqs(request.lista_documentos(), pq);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PutMapping("/actualizar_adjunto")
    public ResponseEntity<?> updateAdjunto(@RequestBody UpdateAdjuntoRequest request) {
        try {
            if (request.pqId() == null || request.pqId() <= 0) {
                return ResponseEntityUtil.handleBadRequest("El id de la PQ es requerido");
            }

            if(pqService.getById(request.pqId()) == null){
                return ResponseEntityUtil.handleNotFoundError("La peticion con el numero de radicado " + request.pqId() + " no fue encontrada");
            }

            service.updateAdjunto(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
