package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.service.AdjuntosPqService;
import com.pqrsdf.pqrsdf.service.PersonasService;
import com.pqrsdf.pqrsdf.service.PqsService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PqsController extends GenericController<Pqs, Long>{
    
    private final PqsService service;
    private final PersonasService personasService;
    private final AdjuntosPqService adjuntosPqService;
    

    public PqsController(PqsService service, PersonasService personasService,
                        AdjuntosPqService adjuntosPqService) {
        super(service);
        this.service = service;
        this.personasService = personasService;
        this.adjuntosPqService = adjuntosPqService;
    }


    @PostMapping("/nologinpq")
    public ResponseEntity<?> getMethodName(@RequestBody NoLoginPq noLoginPq) {
        try {
            Personas persona = personasService.findByTipoDocAndNumDoc(noLoginPq.tipo_doc_id(), noLoginPq.dni());
            if(persona != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(service.oldUser(persona, noLoginPq));
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(service.usuarioNuevo(noLoginPq));
            }
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
    

}
