package com.pqrsdf.pqrsdf.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Municipio;
import com.pqrsdf.pqrsdf.service.MunicipioService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/municipios")
@Tag(name = "Gestion de Municipios")
public class MunicipioController extends GenericController<Municipio, Long> {

    private final MunicipioService service;

    public MunicipioController(MunicipioService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/municipios_departamento")
    public ResponseEntity<?> getMunicipiosConDepartamento(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = true) Long departamentoId) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
            return ResponseEntityUtil.handlePaginationRequest(service.findByDepartamentoId(departamentoId, pageable));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

}
