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
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.repository.PqsRepository;


@Service
public class PqsService extends GenericService<Pqs, Long> {
    
    private final AdjuntosPqService adjuntosPqService;
    private final PersonasService personasService;
    private final PqsRepository repository;
    
    public PqsService(PqsRepository repository, PersonasService personasService,
                        AdjuntosPqService adjuntosPqService) {
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

    @Transactional
    public String oldUser(Personas persona, NoLoginPq noLoginPq) throws IOException {
        Pqs pq = createPq(persona.getId(), noLoginPq);

        if(!noLoginPq.Adjuntos().isEmpty()){
            adjuntosPqService.createAdjuntosPqs(noLoginPq.Adjuntos(), pq.getId());
        }


        return pq.getConsecutivo();
    }

    @Transactional
    public String usuarioNuevo(NoLoginPq noLoginPq) {
        Personas persona = Personas.builder()
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

    public Pqs createPq(Long personaId, NoLoginPq noLoginPq) {
        Pqs pqs = Pqs.builder()
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

    public List<Pqs> findByResponsableId(Long responsableId) {
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
