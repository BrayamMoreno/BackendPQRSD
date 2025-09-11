package com.pqrsdf.pqrsdf.models;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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

    @Column(unique = true, name = "numero_radicado")
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

    @Column(name = "fecha_radicacion")
    private Date fechaRadicacion;

    @Column(name = "hora_radicacion")
    private Time horaRadicacion;

    @Column(name = "fecha_resolucion_estimada")
    private Date fechaResolucionEstimada;
    private Date fechaResolucion;

    @ManyToOne
    @JoinColumn(name = "radicador_id")
    private Persona radicador;

    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsablePQ responsable;

    @Formula("(SELECT he.estado_id FROM historial_estados_pq he WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private Long ultimoEstadoId;

    @Formula("(SELECT epq.nombre FROM historial_estados_pq he JOIN estados_pq epq ON epq.id = he.estado_id WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private String nombreUltimoEstado;

    private Boolean web;

    @OneToMany(mappedBy = "pq", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaCambio ASC")
    private List<HistorialEstadoPQ> historialEstados;

    @OneToMany(mappedBy = "pq", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdjuntoPQ> adjuntos;

}
