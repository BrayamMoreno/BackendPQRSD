package com.pqrsdf.pqrsdf.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Pqs extends GenericEntity{
    
    private Boolean web;

    private String consecutivo;

    @Column(name = "numero_radicado")
    private String numeroRadicado;

    @Column(name = "numero_folio")
    private long numeroFolio;
    
    @Column(name = "detalle_asunto")
    private String detalleAsunto;

    @Column(name = "tipo_pq_id")
    private Long tipoPqId;

    @Column(name = "solicitante_id")
    private Long solicitanteId;

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

    @Column(name = "responsable_id")
    private Long responsableId;
}
