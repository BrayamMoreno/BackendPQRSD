package com.pqrsdf.pqrsdf.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.Specifications.ResponsablePqSpecification;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;
import com.pqrsdf.pqrsdf.service.ResponsablePQService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/responsables_pqs")
@Tag(name = "Gestion de Responsable de Pqs")
public class ResponsablePQController extends GenericController<ResponsablePQ, Long> {

    private final ResponsablePQService service;

    public ResponsablePQController(ResponsablePQService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long areaId) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

            Specification<ResponsablePQ> spec = Specification
                    .where(ResponsablePqSpecification.hasArea(areaId))
                    .and(ResponsablePqSpecification.hasName(search));

            if (service.findAll(spec, pageable).hasContent() == false) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.ok(service.findAll(spec, pageable));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
