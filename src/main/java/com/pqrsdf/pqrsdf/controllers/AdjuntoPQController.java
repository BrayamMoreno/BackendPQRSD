package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.service.AdjuntoPQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "/api/adjuntosPq")
@Tag(name = "Gestion de Adjuntos de PQ")
public class AdjuntoPQController extends GenericController<AdjuntoPQ, Long> {

    private final AdjuntoPQService service;

    public AdjuntoPQController(AdjuntoPQService service) {
        super(service);
        this.service = service;
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
}
