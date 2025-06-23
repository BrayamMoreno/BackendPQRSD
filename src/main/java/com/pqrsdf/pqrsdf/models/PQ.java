package com.pqrsdf.pqrsdf.models;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @ManyToOne @JoinColumn(name = "tipo_pq_id")
    private TipoPQ tipoPQ;

    @ManyToOne @JoinColumn(name = "solicitante_id")
    private Persona solicitante;

    private LocalDate fechaRadicacion;
    private LocalTime horaRadicacion;
    private LocalDate fechaResolucionEstimada;
    private LocalDate fechaResolucion;

    @ManyToOne @JoinColumn(name = "radicador_id")
    private Persona radicador;

    @ManyToOne @JoinColumn(name = "responsable_id")
    private ResponsablePQ responsable;

    private Boolean web;
}