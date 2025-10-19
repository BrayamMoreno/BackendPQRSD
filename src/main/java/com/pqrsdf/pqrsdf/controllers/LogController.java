package com.pqrsdf.pqrsdf.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.Specifications.LogSpecification;
import com.pqrsdf.pqrsdf.models.AuditLog;
import com.pqrsdf.pqrsdf.service.LogService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<?> getLogs(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin)
        {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        Specification<AuditLog> spec = Specification
            .where(LogSpecification.hasFechaRango(fechaInicio, fechaFin))
            .and(LogSpecification.hasUsuarioOrAccionEndpoitStatusCode(search));

        return ResponseEntityUtil.handlePaginationRequest(
            logService.getLogs(spec, pageable));
    }

}
