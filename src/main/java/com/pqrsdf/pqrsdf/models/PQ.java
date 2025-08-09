package com.pqrsdf.pqrsdf.models;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Formula;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pqs")
public class PQ extends GenericEntity {

    @Column(unique = true)
    private String consecutivo;

    @Column(unique = true)
    private String numeroRadicado;

    private Integer numeroFolio;
    private String detalleAsunto;

    private String detalleDescripcion;

    @ManyToOne
    @JoinColumn(name = "tipo_pq_id")
    private TipoPQ tipoPQ;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")

    private Persona solicitante;

    private LocalDate fechaRadicacion;
    private LocalTime horaRadicacion;
    private LocalDate fechaResolucionEstimada;
    private LocalDate fechaResolucion;

    @ManyToOne
    @JoinColumn(name = "radicador_id")
    private Persona radicador;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsablePQ responsable;

    @Formula("(SELECT he.estado_id FROM historial_estados_pq he WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private Long ultimoEstadoId;

    @Formula("(SELECT epq.nombre FROM historial_estados_pq he JOIN estados_pq epq ON epq.id = he.estado_id WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private String nombreUltimoEstado;

    private Boolean web;
}