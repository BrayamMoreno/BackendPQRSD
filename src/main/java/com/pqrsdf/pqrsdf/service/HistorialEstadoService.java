package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.dto.HistorialEstadoRequest;
import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.EstadoPQ;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;
import com.pqrsdf.pqrsdf.models.PQ;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.repository.EstadoPQRepository;
import com.pqrsdf.pqrsdf.repository.HistorialEstadosRespository;
import com.pqrsdf.pqrsdf.repository.UsuarioRepository;

@Service
public class HistorialEstadoService extends GenericService<HistorialEstadoPQ, Long> {

    private final HistorialEstadosRespository repository;
    private final EstadoPQRepository estadoPQRepository;
    private final UsuarioRepository usuarioRepository;

    public HistorialEstadoService(HistorialEstadosRespository repository, EstadoPQRepository estadoPQRepository, UsuarioRepository usuarioRepository) {
        super(repository);
        this.repository = repository;
        this.estadoPQRepository = estadoPQRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void crearHistorial(HistorialEstadoRequest request, PQ pq) {
        // Buscar estado
        EstadoPQ estado = estadoPQRepository.findById(request.estado().id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Estado no encontrado con id: " + request.estado().id()));

        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(request.usuario().id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuario no encontrado con id: " + request.usuario().id()));

        // Crear entidad
        HistorialEstadoPQ historial = new HistorialEstadoPQ();
        historial.setPq(pq);
        historial.setNumeroRadicado(request.numeroRadicado());
        historial.setEstado(estado);
        historial.setObservacion(request.observacion());
        historial.setCambiadoPor(usuario);

        repository.save(historial);
    }

    public void actualizarHistorial(HistorialEstadoRequest request, PQ pq) {
        // Buscar el historial existente
        HistorialEstadoPQ historial = repository.findById(pq.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Historial no encontrado para PQ ID: " + pq.getId() + " y Numero Radicado: " + request.numeroRadicado()));

        // Buscar estado
        EstadoPQ estado = estadoPQRepository.findById(request.estado().id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Estado no encontrado con id: " + request.estado().id()));

        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(request.usuario().id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuario no encontrado con id: " + request.usuario().id()));

        // Actualizar campos
        historial.setEstado(estado);
        historial.setObservacion(request.observacion());
        historial.setCambiadoPor(usuario);

        repository.save(historial);
    }

}
