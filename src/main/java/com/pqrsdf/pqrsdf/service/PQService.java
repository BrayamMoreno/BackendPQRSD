package com.pqrsdf.pqrsdf.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pqrsdf.pqrsdf.dto.DocumentoDTO;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AdjuntoPQ;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.repository.PQRepository;

import io.github.cdimascio.dotenv.Dotenv;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;

@Service
public class PQService extends GenericService<PQ, Long> {

    private final AdjuntoPQService adjuntosPqService;
    private final HistorialEstadoService historialEstadoService;
    private final PersonaService personasService;
    private final PQRepository repository;
    private final TipoPQService tipoPqService;
    private final EstadoPQService estadoPQService;
    private final HistorialEstadosRespository historialEstadosRespository;
    private final MinioClient minioClient;

    private String BUCKET_NAME;

    public PQService(PQRepository repository, PersonaService personasService,
            AdjuntoPQService adjuntosPqService, TipoPQService tipoPqService,
            HistorialEstadoService historialEstadoService, EstadoPQService estadoPQService,
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

    public Page<PQ> findProximasAVencer(Pageable pageable) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7); // Por ejemplo, 30 días a partir de hoy
        return repository.findByFechaResolucionEstimadaBetween(hoy, limite, pageable);
    }

    public Page<PQ> findBySolicitanteId(Long solicitanteId, Pageable pageable) {
        return repository.findBySolicitanteIdOrderByFechaRadicacionDesc(solicitanteId, pageable);
    }

    public Page<PQ> findByFechaRadicacionDes(Pageable pageable) {
        return repository.findAllByOrderByFechaRadicacionDesc(pageable);
    }

    public Page<PQ> findByResponsableId(Long responsableId, Pageable pageable) {
        return repository.findByResponsableId(responsableId, pageable);
    }

    public List<Map<String, Object>> obtenerTendenciasDiarias() {
        LocalDate hoy = LocalDate.now();
        LocalDate hace7Dias = hoy.minusDays(6);

        List<PQ> ultimos7Dias = repository.findUltimos7Dias(hace7Dias);

        // Inicializar los 7 días con cantidad 0
        Map<LocalDate, Long> conteo = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            conteo.put(hoy.minusDays(i), 0L);
        }

        // Contar solicitudes por fecha
        ultimos7Dias.forEach(pq -> {
            LocalDate fecha = pq.getFechaRadicacion();
            conteo.computeIfPresent(fecha, (k, v) -> v + 1);
        });

        // Convertir al formato requerido (día completo con mayúscula inicial)
        List<Map<String, Object>> tendencias = conteo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    String diaSemana = e.getKey()
                            .getDayOfWeek()
                            .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                    // Capitalizar primera letra
                    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);
                    map.put("fecha", diaSemana);
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return tendencias;
    }

    public List<Map<String, Object>> obtenerConteoPorTipoMes() {
        LocalDate ahora = LocalDate.now();
        LocalDate inicioMes = ahora.withDayOfMonth(1);
        LocalDate finMes = ahora.withDayOfMonth(ahora.lengthOfMonth());

        List<Object[]> resultados = repository.contarPorTipoEnMes(inicioMes, finMes);

        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("tipo", fila[0]);
            map.put("cantidad", ((Number) fila[1]).longValue());
            lista.add(map);
        }
        return lista;
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

        System.out.println("Hora local servidor: " + LocalTime.now());
        System.out.println("Zona horaria servidor: " + ZoneId.systemDefault());

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

                    String basePath = "documentos/" + pq.getSolicitante().getDni() + "/";
                    String originalName = doc.Nombre();
                    String objectName = basePath + originalName;

                    // Verifica si ya existe un objeto con el mismo nombre
                    int counter = 1;
                    while (objectExists(BUCKET_NAME, objectName)) {
                        String extension = "";
                        String nameWithoutExtension = originalName;

                        int dotIndex = originalName.lastIndexOf('.');
                        if (dotIndex != -1) {
                            extension = originalName.substring(dotIndex);
                            nameWithoutExtension = originalName.substring(0, dotIndex);
                        }

                        String newName = nameWithoutExtension + " (" + counter + ")" + extension;
                        objectName = basePath + newName;
                        counter++;
                    }

                    // Subir el archivo con el nombre ajustado
                    inputStream.reset(); // Por si acaso
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(BUCKET_NAME)
                                    .object(objectName)
                                    .stream(new ByteArrayInputStream(data), data.length, -1)
                                    .contentType(doc.Tipo())
                                    .build());

                    // Guardar en la base de datos
                    AdjuntoPQ adjuntoPQ = AdjuntoPQ.builder()
                            .pq(pq)
                            .rutaArchivo(objectName)
                            .nombreArchivo(objectName.substring(basePath.length())) // nombre sin ruta
                            .build();

                    adjuntosPqService.createEntity(adjuntoPQ);

                } catch (Exception e) {
                    throw new RuntimeException("Error al guardar el adjunto: " + doc.Nombre(), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error general al procesar los adjuntos", e);
        }
    }

    private boolean objectExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true; // Existe
        } catch (io.minio.errors.ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false; // No existe
            }
            throw new RuntimeException("Error al verificar existencia del archivo", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del archivo", e);
        }
    }

}
