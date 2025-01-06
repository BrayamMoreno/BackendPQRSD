package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Municipios;
import com.pqrsdf.pqrsdf.service.MunicipiosService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/api/municipios")
@Tag(name = "Gestion de Municipios")
public class MunicipiosController extends GenericController<Municipios, Long> {

    private final MunicipiosService service;

    public MunicipiosController(MunicipiosService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/mpd_data")
    public ResponseEntity<?> getMunicipiosByDepartamento(
        @RequestParam Long departamentoId,
        @RequestParam(required = false, defaultValue = "id") String order_by,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size)
    {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Municipios> municipios = service.findByDepartamentoId(departamentoId, pageable);

            if (municipios != null) {
                return ResponseEntityUtil.handlePaginationRequest(municipios);
            } else {
                return ResponseEntityUtil.handleNotFoundError(
                        "No se encontraron municipios para el departamento con id: " + departamentoId);
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

}
