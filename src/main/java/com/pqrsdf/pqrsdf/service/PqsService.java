package com.pqrsdf.pqrsdf.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialTelefonos;
import com.pqrsdf.pqrsdf.models.HistorialCorreos;
import com.pqrsdf.pqrsdf.models.Personas;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.repository.PqsRepository;


@Service
public class PqsService extends GenericService<Pqs, Long> {
    
    private final HistorialCorreoService historialCorreoService;
    private final HistorialTelefonoService historialTelefonoService;
    private final AdjuntosPqService adjuntosPqService;
    private final PersonasService personasService;
    private final PqsRepository repository;
    
    public PqsService(PqsRepository repository, PersonasService personasService,
                        HistorialCorreoService historialCorreoService,
                        HistorialTelefonoService historialTelefonoService,
                        AdjuntosPqService adjuntosPqService) {
        super(repository);
        this.historialCorreoService = historialCorreoService;
        this.historialTelefonoService = historialTelefonoService;
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
        if(!persona.getCorreo().equals(noLoginPq.correo())) {
            HistorialCorreos correos = HistorialCorreos.builder()
                    .correo(persona.getCorreo())
                    .personaId(persona.getId())
            .build();
        
            historialCorreoService.createEntity(correos);
            personasService.updateCorreo(persona, noLoginPq.correo());
        }

        if(!persona.getTelefono().equals(noLoginPq.telefono())) {
            HistorialTelefonos telefonos = HistorialTelefonos.builder()
                    .telefono(persona.getTelefono())
                    .personaId(persona.getId())
            .build();

            historialTelefonoService.createEntity(telefonos);
            personasService.updateTelefono(persona, noLoginPq.telefono());
        }
        
        Pqs pq = createPq(persona.getId(), noLoginPq);

        if(!noLoginPq.Adjuntos().isEmpty()){
            adjuntosPqService.createAdjuntosPqs(noLoginPq.Adjuntos(), pq.getId());
        }


        return pq.getIdentificador();
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
                .correo(noLoginPq.correo())
                .direccion(noLoginPq.direccion())
                .anonimo(true)
        .build();

        persona = personasService.createEntity(persona);

        Pqs pqs = createPq(persona.getId(), noLoginPq);

        return pqs.getIdentificador();
    }

    public Pqs createPq(Long personaId, NoLoginPq noLoginPq) {
        Pqs pqs = Pqs.builder()
                .web(true)
                .detalleAsunto(noLoginPq.asunto())
                .TipoPqId(noLoginPq.tipo_pq_id())
                .solicitanteId(personaId)
                .horaRadicacion(LocalTime.now())
                .fechaRadicacion(LocalDate.now())
                .identificador(generarIdentificadorPQ())
        .build();

        return repository.save(pqs);
    }
}
