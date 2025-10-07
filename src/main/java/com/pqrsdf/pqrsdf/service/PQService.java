package com.pqrsdf.pqrsdf.service;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pqrsdf.pqrsdf.dto.ConteoPQDTO;
import com.pqrsdf.pqrsdf.dto.DocumentoDTO;
import com.pqrsdf.pqrsdf.dto.PqDto;
import com.pqrsdf.pqrsdf.dto.PqResponseDto;
import com.pqrsdf.pqrsdf.dto.RadicarDto;
import com.pqrsdf.pqrsdf.dto.ResolucionDto;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.repository.PQRepository;
import com.pqrsdf.pqrsdf.repository.ResponsablePQRepository;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class PQService extends GenericService<PQ, Long> {

    private final AdjuntoPQService adjuntosPqService;
    private final UsuarioRepository usuarioRepository;
    private final ResponsablePQRepository responsablePQRepository;
    private final PersonaService personasService;
    private final PQRepository repository;
    private final TipoPQService tipoPqService;
    private final EstadoPQService estadoPQService;
    private final HistorialEstadosRespository historialEstadosRespository;
    private final EmailService emailService;

    public PQService(PQRepository repository, PersonaService personasService,
            AdjuntoPQService adjuntosPqService, TipoPQService tipoPqService, EstadoPQService estadoPQService,
            HistorialEstadosRespository historialEstadosRespository,
            Dotenv dotenv, ResponsablePQRepository responsablePQRepository, UsuarioRepository usuarioRepository,
            EmailService emailService) {
        super(repository);
        this.personasService = personasService;
        this.repository = repository;
        this.adjuntosPqService = adjuntosPqService;
        this.tipoPqService = tipoPqService;
        this.estadoPQService = estadoPQService;
        this.historialEstadosRespository = historialEstadosRespository;
        this.responsablePQRepository = responsablePQRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    public String generarIdentificadorPQ() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String fecha = sdf.format(new Date());

        String uuid = UUID.randomUUID().toString();
        String ultimos8 = uuid.substring(uuid.length() - 8);

        return "STTG".concat("-").concat(fecha).concat("-").concat(ultimos8);
    }

    public Page<PQ> findVencidas(Pageable pageable, Specification<PQ> spec) {
        LocalDate hoy = LocalDate.now();

        // Condición: fechaResolucionEstimada < hoy
        Specification<PQ> fechaSpec = (root, query, cb) -> cb.lessThan(root.get("fechaResolucionEstimada"), hoy);
        Specification<PQ> finalSpec = spec == null ? fechaSpec : spec.and(fechaSpec);

        return repository.findAll(finalSpec, pageable);
    }

    public Page<PQ> findProximasAVencer(Pageable pageable, Specification<PQ> spec) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7); // Por ejemplo, 30 días a partir de hoy

        Specification<PQ> fechaSpec = (root, query, cb) -> cb.between(root.get("fechaResolucionEstimada"), hoy, limite);
        Specification<PQ> finalSpec = spec == null ? fechaSpec : spec.and(fechaSpec);

        return repository.findAll(finalSpec, pageable);
    }

    public Page<PQ> findAll(Pageable pageable, Specification<PQ> spec) {
        return repository.findAll(
                spec, pageable);
    }

    public Page<PqResponseDto> findAllPage(Pageable pageable, Specification<PQ> spec) {
        Page<PQ> page = repository.findAll(spec, pageable);
        return page.map(pq -> new PqResponseDto(
                pq.getId(),
                pq.getConsecutivo(),
                pq.getNumeroRadicado(),
                pq.getNumeroFolio(),
                pq.getDetalleAsunto(),
                pq.getDetalleDescripcion(),
                pq.getFechaRadicacion(),
                pq.getHoraRadicacion(),
                pq.getFechaResolucionEstimada(),
                pq.getFechaResolucion(),
                pq.getRespuesta(),
                pq.getWeb(),
                pq.getUltimoEstadoId(),
                pq.getNombreUltimoEstado(),
                pq.getTipoPQ(),
                pq.getAdjuntos()));
    }

    public List<Map<String, Object>> obtenerTendenciasDiarias() {
        LocalDate hoy = LocalDate.now();
        LocalDate hace7Dias = hoy.minusDays(6);

        // Si tu repositorio espera Date, convierte aquí
        Date hace7DiasDate = Date.from(hace7Dias.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<PQ> ultimos7Dias = repository.findUltimos7Dias(hace7DiasDate);

        // Inicializar los 7 días con cantidad 0
        Map<LocalDate, Long> conteo = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            conteo.put(hoy.minusDays(i), 0L);
        }

        // Contar solicitudes por fecha
        ultimos7Dias.forEach(pq -> {
            LocalDate fecha = pq.getFechaRadicacion()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            conteo.computeIfPresent(fecha, (k, v) -> v + 1);
        });

        // Convertir al formato requerido
        return conteo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    String diaSemana = e.getKey()
                            .getDayOfWeek()
                            .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
                    diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);
                    map.put("fecha", diaSemana);
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
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
                .numeroRadicado(generarIdentificadorPQ())
                .detalleAsunto(form.detalleAsunto())
                .detalleDescripcion(form.detalleDescripcion())
                .tipoPQ(tipoPqService.getById(Long.parseLong(form.tipo_pq_id())))
                .solicitante(personasService.getById(Long.parseLong(form.solicitante_id())))
                .fechaRadicacion(Date.from(Instant.now()))
                .horaRadicacion(Time.valueOf(LocalTime.now()))
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

    @Transactional
    public List<File> createAdjuntosPqs(List<DocumentoDTO> files, PQ pq) {
        try {
            return adjuntosPqService.createAdjuntosPqs(files, pq);
        } catch (Exception e) {
            throw new RuntimeException("Error general al procesar los adjuntos", e);
        }
    }

    public void aceptarRechazarPq(RadicarDto entity) {
        if (entity.isAprobada()) {
            isAprobada(entity);
        } else {
            isNotAprobada(entity);
        }
    }

    @Transactional
    public void isAprobada(RadicarDto entity) {
        PQ pq = repository.findById(entity.solicitudId())
                .orElseThrow(() -> new RuntimeException("PQ no encontrado con ID: " + entity.solicitudId()));

        pq.setResponsable(responsablePQRepository.findById(entity.responsableId())
                .orElseThrow(
                        () -> new RuntimeException("Responsable no encontrado con ID: " + entity.responsableId())));

        Date fechaResolucionEstimdad = darFechaResolucion(pq);

        HistorialEstadoPQ historialEstadoPQ = HistorialEstadoPQ.builder()
                .usuario(usuarioRepository.findById(entity.responsableId())
                        .orElseThrow(
                                () -> new RuntimeException("Usuario no encontrado con ID: " + entity.radicadorId())))
                .estado(estadoPQService.getById(2L))
                .pq(pq)
                .fechaCambio(new java.sql.Timestamp(System.currentTimeMillis()))
                .observacion(entity.comentario())
                .build();

        pq.setFechaResolucionEstimada(fechaResolucionEstimdad);
        repository.save(pq);
        historialEstadosRespository.save(historialEstadoPQ);
    }

    private Date darFechaResolucion(PQ pq) {
        HolidayManager manager = HolidayManager.getInstance(
                ManagerParameters.create("co"));

        Long diasHabiles = tipoPqService
                .getById(pq.getTipoPQ().getId())
                .getDiasHabilesRespuesta();

        LocalDate fechaResolucion = Instant.now()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        int agregados = 0;
        while (agregados < diasHabiles) {
            fechaResolucion = fechaResolucion.plusDays(1);

            boolean esFinDeSemana = fechaResolucion.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    fechaResolucion.getDayOfWeek() == DayOfWeek.SUNDAY;

            boolean esFestivo = manager.isHoliday(fechaResolucion);

            if (!esFinDeSemana && !esFestivo) {
                agregados++;
            }
        }

        // Convertir de nuevo a java.util.Date antes de retornar
        return Date.from(fechaResolucion.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Transactional
    public void isNotAprobada(RadicarDto entity) {

        PQ pq = repository.findById(entity.solicitudId())
                .orElseThrow(() -> new RuntimeException("PQ no encontrado con ID: " + entity.solicitudId()));

        pq.setRespuesta(entity.motivoRechazo());

        repository.save(pq);

        HistorialEstadoPQ historialEstadoPQ = HistorialEstadoPQ.builder()
                .usuario(usuarioRepository.findById(entity.radicadorId())
                        .orElseThrow(
                                () -> new RuntimeException("Usuario no encontrado con ID: " + entity.radicadorId())))
                .estado(estadoPQService.getById(3L))
                .pq(repository.findById(entity.solicitudId())
                        .orElseThrow(() -> new RuntimeException("PQ no encontrado con ID: " + entity.solicitudId())))
                .fechaCambio(new java.sql.Timestamp(System.currentTimeMillis()))
                .observacion(entity.comentario())
                .build();

        historialEstadosRespository.save(historialEstadoPQ);
    }

    @Transactional
    public void darResolucion(ResolucionDto resolucionDto) {
        PQ pq = repository.findById(resolucionDto.pqId())
                .orElseThrow(() -> new RuntimeException("PQ no encontrado con ID: " + resolucionDto.pqId()));

        HistorialEstadoPQ historialEstadoPQ = HistorialEstadoPQ.builder()
                .usuario(usuarioRepository.findById(resolucionDto.responsableId())
                        .orElseThrow(() -> new RuntimeException(
                                "Usuario no encontrado con ID: " + resolucionDto.responsableId())))
                .estado(estadoPQService.getById(4L))
                .pq(pq)
                .fechaCambio(new java.sql.Timestamp(System.currentTimeMillis()))
                .observacion(resolucionDto.comentario())
                .build();

        historialEstadosRespository.save(historialEstadoPQ);

        pq.setRespuesta(resolucionDto.respuesta());
        List<File> adjuntos = createAdjuntosPqs(resolucionDto.lista_documentos(), pq);

        emailService.sendEmailAdjuntos(pq.getSolicitante(), pq.getNumeroRadicado(), resolucionDto.listaCorreos(),
                resolucionDto.asunto(), resolucionDto.respuesta(), adjuntos);

        repository.save(pq);

    }

    public void sendEmail(List<String> to, String subject, String body) {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("La lista de destinatarios no puede estar vacía");
        }

        if (!validateTo(to)) {
            throw new IllegalArgumentException("La lista de destinatarios contiene direcciones de correo no válidas");
        }

        if (to.size() > 0) {
            System.out.println();
        } else {

        }
    }

    public boolean validateTo(List<String> to) {
        for (String email : to) {
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return false;
            }
        }
        return true;
    }

    public ConteoPQDTO obtenerConteoPorSolicitante(Long solicitanteId) {
        return repository.contarPorSolicitante(solicitanteId);
    }

}
