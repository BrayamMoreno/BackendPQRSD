package com.pqrsdf.pqrsdf.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import com.pqrsdf.pqrsdf.dto.NoLoginPq;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.EstadoPQ;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.repository.PQRepository;

@Service
public class PQService extends GenericService<PQ, Long> {

    private final AdjuntoPQService adjuntosPqService;
    private final HistoralEstadoService historialEstadoService;
    private final PersonaService personasService;
    private final PQRepository repository;
    private final TipoPQService tipoPqService;
    private final EstadoPQService estadoPQService;
    private final HistorialEstadosRespository historialEstadosRespository;

    public PQService(PQRepository repository, PersonaService personasService,
            AdjuntoPQService adjuntosPqService, TipoPQService tipoPqService,
            HistoralEstadoService historialEstadoService, EstadoPQService estadoPQService,
            HistorialEstadosRespository historialEstadosRespository) {
        super(repository);
        this.personasService = personasService;
        this.repository = repository;
        this.adjuntosPqService = adjuntosPqService;
        this.tipoPqService = tipoPqService;
        this.historialEstadoService = historialEstadoService;
        this.estadoPQService = estadoPQService;
        this.historialEstadosRespository = historialEstadosRespository;
    }

    public String generarIdentificadorPQ() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fecha = sdf.format(new Date());

        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return "PQ-" + fecha + "-" + uuid;
    }

    public Page<PQ> findBySolicitanteId(Long solicitanteId, Pageable pageable) {
        return repository.findBySolicitanteId(solicitanteId, pageable);
    }

    /**
     * @Transactional
     *                public String oldUser(Persona persona, NoLoginPq noLoginPq)
     *                throws IOException {
     *                PQ pq = createPq(persona.getId(), noLoginPq);
     * 
     *                if(!noLoginPq.Adjuntos().isEmpty()){
     *                adjuntosPqService.createAdjuntosPqs(noLoginPq.Adjuntos(),
     *                pq.getId());
     *                }
     * 
     * 
     *                return pq.getConsecutivo();
     *                }
     */

    public Page<PQ> findByResponsableId(Long responsableId, Pageable pageable) {
        return repository.findByResponsableId(responsableId, pageable);
    }

    @Transactional
    public PQ createPq(PqDto form) {

        PQ pq = PQ.builder()
                .consecutivo(generarIdentificadorPQ())
                .detalleAsunto(form.detalleAsunto())
                .detalleDescripcion(form.detalleDescripcion())
                .tipoPQ(tipoPqService.getById(Long.parseLong(form.tipo_pq_id())))
                .solicitante(personasService.getById(Long.parseLong(form.solicitante_id())))
                .horaRadicacion(LocalTime.now())
                .fechaRadicacion(LocalDate.now())
                .web(true)
                .build();

        HistorialEstadoPQ historialEstadoPQ = HistorialEstadoPQ.builder()
                .estado(estadoPQService.getById(1L))
                .pq(pq)
                .fechaCambio(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();

        pq = repository.save(pq);
        historialEstadosRespository.save(historialEstadoPQ);
        return pq;
    }

    /**
     * public HashMap<String, String> countEstadosPq(Long id) {
     * HashMap<String, String> estados = new HashMap<>();
     * List<Pqs> pqList = repository.findByResponsableId(id);
     * 
     * for (Pqs pq : pqList) {
     * String estado = pq.get
     * estados.put(estado, estados.getOrDefault(estado, "0"));
     * }
     * 
     * return null;
     * }
     */
}
