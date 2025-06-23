package com.pqrsdf.pqrsdf.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.repository.PQRepository;


@Service
public class PQService extends GenericService<PQ, Long> {
    
    private final AdjuntoPQService adjuntosPqService;
    private final PersonaService personasService;
    private final PQRepository repository;
    
    public PQService(PQRepository repository, PersonaService personasService,
                        AdjuntoPQService adjuntosPqService) {
        super(repository);
        this.personasService = personasService;
        this.repository = repository;
        this.adjuntosPqService = adjuntosPqService;
    }

    public String generarIdentificadorPQ() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fecha = sdf.format(new Date());

        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return "PQ-" + fecha + "-" + uuid;
    }

    /**
    @Transactional
    public String oldUser(Persona persona, NoLoginPq noLoginPq) throws IOException {
        PQ pq = createPq(persona.getId(), noLoginPq);

        if(!noLoginPq.Adjuntos().isEmpty()){
            adjuntosPqService.createAdjuntosPqs(noLoginPq.Adjuntos(), pq.getId());
        }


        return pq.getConsecutivo();
    }

    
    @Transactional
    public String usuarioNuevo(NoLoginPq noLoginPq) {
        Persona persona = Persona.builder()
                .tipoDoc(noLoginPq.tipo_doc_id())
                .dni(noLoginPq.dni())
                .tipoPersona(noLoginPq.tipo_persona_id())
                .genero(noLoginPq.genero())
                .municipioId(noLoginPq.municipio_id())
                .nombre(noLoginPq.nombres())
                .apellido(noLoginPq.apellidos())
                .telefono(noLoginPq.telefono())
                .direccion(noLoginPq.direccion())
        .build();

        persona = personasService.createEntity(persona);

        Pqs pqs = createPq(persona.getId(), noLoginPq);

        return pqs.getConsecutivo();
    }
    

    public PQ createPq(Long personaId, NoLoginPq noLoginPq) {
        PQ pqs = PQ.builder()
                .web(true)
                .detalleAsunto(noLoginPq.asunto())
                .tipoPqId(noLoginPq.tipo_pq_id())
                .solicitanteId(personaId)
                .horaRadicacion(LocalTime.now())
                .fechaRadicacion(LocalDate.now())
                .consecutivo(generarIdentificadorPQ())
        .build();

        return repository.save(pqs);
    }
    */

    public List<PQ> findByResponsableId(Long responsableId) {
        return repository.findByResponsableId(responsableId);
    }

    /**
    public HashMap<String, String> countEstadosPq(Long id) {
        HashMap<String, String> estados = new HashMap<>();
        List<Pqs> pqList = repository.findByResponsableId(id);

        for (Pqs pq : pqList) {
            String estado = pq.get
            estados.put(estado, estados.getOrDefault(estado, "0"));
        }

        return null;
    }
     */
}
