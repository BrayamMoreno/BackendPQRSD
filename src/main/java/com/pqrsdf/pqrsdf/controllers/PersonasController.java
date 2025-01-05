package com.pqrsdf.pqrsdf.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.service.PersonasService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

@RestController
@RequestMapping(path = "/api/personas")
public class PersonasController extends GenericController<Personas, Long> {

    private final PersonasService service;

    public PersonasController(PersonasService service){
        super(service);
        this.service = service;
    }

    @GetMapping("/data")
    public ResponseEntity<?> PersonData(@RequestParam Long tipoDoc, @RequestParam String dni) {
        try {
            Personas persona = service.findByTipoDocAndNumDoc(tipoDoc, dni);
            if(persona != null) {
                return ResponseEntity.status(HttpStatus.OK).body(persona);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro la persona");
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

}
