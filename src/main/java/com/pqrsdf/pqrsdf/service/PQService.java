package com.pqrsdf.pqrsdf.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pqrsdf.pqrsdf.dto.DocumentoDTO;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.repository.PQRepository;

import io.github.cdimascio.dotenv.Dotenv;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class PQService extends GenericService<PQ, Long> {

    private final AdjuntoPQService adjuntosPqService;
    private final HistoralEstadoService historialEstadoService;
    private final PersonaService personasService;
    private final PQRepository repository;
    private final TipoPQService tipoPqService;
    private final EstadoPQService estadoPQService;
    private final HistorialEstadosRespository historialEstadosRespository;
    private final MinioClient minioClient;

    private String BUCKET_NAME;

    public PQService(PQRepository repository, PersonaService personasService,
            AdjuntoPQService adjuntosPqService, TipoPQService tipoPqService,
            HistoralEstadoService historialEstadoService, EstadoPQService estadoPQService,
            HistorialEstadosRespository historialEstadosRespository, MinioClient minioClient,
            Dotenv dotenv) {
        super(repository);
        this.personasService = personasService;
        this.repository = repository;
        this.adjuntosPqService = adjuntosPqService;
        this.tipoPqService = tipoPqService;
        this.historialEstadoService = historialEstadoService;
        this.estadoPQService = estadoPQService;
        this.historialEstadosRespository = historialEstadosRespository;
        this.minioClient = minioClient;

        this.BUCKET_NAME = dotenv.get("MINIO_BUCKET_NAME");
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

        if (form.lista_documentos() != null) {
            createAdjuntosPqs(form.lista_documentos(), pq);
        }
            return pq;
    }

    public void createAdjuntosPqs(List<DocumentoDTO> files, PQ pq) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }

            for (DocumentoDTO doc : files) {
                try {
                    byte[] data = Base64.getDecoder().decode(doc.Contenido());

                    InputStream inputStream = new ByteArrayInputStream(data);
                    String objectName = "documentos/" + pq.getSolicitante().getDni() + "/" + doc.Nombre();

                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(BUCKET_NAME)
                                    .object(objectName)
                                    .stream(inputStream, data.length, -1)
                                    .contentType(doc.Tipo())
                                    .build());
                } catch (Exception e) {
                    throw new RuntimeException("Error al guardar el adjunto: ");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error general al procesar los adjuntos", e);
        }
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
