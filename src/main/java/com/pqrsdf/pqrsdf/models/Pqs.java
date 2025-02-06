package com.pqrsdf.pqrsdf.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pqs")
public class Pqs extends GenericEntity{
    
    private Boolean web;

    private String identificador;

    @Column(name = "numero_radicado")
    private String numeroRadicado;

    @Column(name = "detalle_asunto")
    private String detalleAsunto;

    @Column(name = "tipo_pq_id")
    private Long TipoPqId;

    @Column(name = "solicitante_id")
    private Long solicitanteId;

    @Column(name = "numero_folio")
    private long numeroFolio;

    @Column(name = "fecha_radicacion")
    private LocalDate fechaRadicacion;

    @Column(name = "hora_radicacion ")
    private LocalTime horaRadicacion;

    @Column(name = "fecha_resolucion_estimada")
    private LocalDate fechaResolucionEstimaada;

    @Column(name = "fecha_resolucion")
    private LocalDate fechaResolucion;

    private String respuesta;

    @Column(name = "radicador_id")
    private Long radicadorId;

    @Column(name = "modificador_id")
    private Long modificadorId;
}
