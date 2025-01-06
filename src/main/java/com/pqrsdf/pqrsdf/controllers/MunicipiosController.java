package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Municipios;
import com.pqrsdf.pqrsdf.service.MunicipiosService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/api/municipios")
@Tag(name = "Gestion de Municipios")
public class MunicipiosController extends GenericController<Municipios, Long>{
    
    private final MunicipiosService service;

    public MunicipiosController(MunicipiosService service){
        super(service);
        this.service = service;
    }

    @GetMapping("/mpd_data")
    public ResponseEntity<?> getMunicipiosByDepartamentoId(@RequestParam Long departamentoId) {
        try {
            List<Municipios> municipios = service.findByDepartamentoId(departamentoId);

            if(municipios != null) {
                return ResponseEntity.status(HttpStatus.OK).body(municipios);
            } else {
                return ResponseEntityUtil.handleNotFoundError("No se encontraron municipios para el departamento con id: " + departamentoId);
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
    
}
