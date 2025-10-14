package com.pqrsdf.pqrsdf.controllers;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.service.BackupService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/backups")
@Tag(name = "Backups", description = "Endpoints para la gestión de respaldos")
public class BackupController {

    private final BackupService service;

    public BackupController(BackupService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<?> createBackup() throws IOException {
        try {
                service.createBackup();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping()
    public ResponseEntity<?> listBackups() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.listarBackups());
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<?> deleteBackup(@PathVariable String fileName) {
        try {
            boolean deleted = service.deleteBackup(fileName);
            return deleted
                    ? ResponseEntity.status(HttpStatus.OK).build()
                    : ResponseEntityUtil.handleBadRequest("Error al eliminar el backup ".concat(fileName));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadBackup(@PathVariable String fileName) {
        try {
            Resource resource = service.downloadBackup(fileName);

            if (resource == null || !resource.exists()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el backup: " + fileName);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
