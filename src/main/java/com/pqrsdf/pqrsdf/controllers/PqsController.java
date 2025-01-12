package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.service.IdentificadorPqService;
import com.pqrsdf.pqrsdf.service.PersonasService;
import com.pqrsdf.pqrsdf.service.PqsService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping(path = "/api/pqs")
@Tag(name = "Gestion de Pqs")
public class PqsController extends GenericController<Pqs, Long>{
    
    private final IdentificadorPqService identificadorPqService;
    private final PqsService service;
    private final PersonasService personasService;
    

    public PqsController(PqsService service, PersonasService personasService,
            IdentificadorPqService identificadorPqService)
    {
        super(service);
        this.service = service;
        this.personasService = personasService;
        this.identificadorPqService = identificadorPqService;
    }


    @PostMapping("/nologinpq")
    public ResponseEntity<?> getMethodName(@RequestBody NoLoginPq noLoginPq) {
        
        Personas persona = personasService.findByTipoDocAndNumDoc(noLoginPq.tipo_doc_id(), noLoginPq.dni());

        /**
        if(persona == null){
            persona = new Personas();
            persona.setTipoDoc(noLoginPq.tipo_doc_id());
            persona.setDni(noLoginPq.dni());
            persona.setTipoPersona(noLoginPq.tipo_persona_id());
            persona.setGenero(noLoginPq.genero());
            persona.setMunicipioId(noLoginPq.municipio_id());
            persona.setNombre(noLoginPq.nombre());
            persona.setApellido(noLoginPq.apellido());
            persona.setCelular(noLoginPq.celular());

            persona = personasService.createEntity(persona);

            Pqs pqs = new Pqs();
            pqs.setDetalleAsunto(noLoginPq.asunto());
            pqs.setTipoPqId(noLoginPq.tipo_pq_id());
            pqs.setSolicitanteId(persona.getId());
            pqs.setIdentificador(identificadorPqService.generarIdentificadorPq());
        }
        
        */
        return null;
    }
    
 

}
