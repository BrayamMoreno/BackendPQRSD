package com.pqrsdf.pqrsdf.models;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PreRemove;
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
@SQLDelete(sql = "UPDATE pqs SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PQ extends GenericEntity {

    @Column(unique = true, name = "numero_radicado")
    private String numeroRadicado;

    private String detalleAsunto;

    private String detalleDescripcion;

    @ManyToOne
    @JoinColumn(name = "tipo_pq_id")
    private TipoPQ tipoPQ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id")
    private Persona solicitante;

    @Column(name = "fecha_radicacion")
    private Date fechaRadicacion;

    @Column(name = "hora_radicacion")
    private Time horaRadicacion;

    @Column(name = "fecha_resolucion_estimada")
    private Date fechaResolucionEstimada;
    private Date fechaResolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radicador_id")
    private Persona radicador;

    private String respuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private ResponsablePQ responsable;

    @Formula("(SELECT he.estado_id FROM historial_estados_pq he WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private Long ultimoEstadoId;

    @Formula("(SELECT epq.nombre FROM historial_estados_pq he JOIN estados_pq epq ON epq.id = he.estado_id WHERE he.pq_id = id ORDER BY he.fecha_cambio DESC LIMIT 1)")
    private String nombreUltimoEstado;

    private Boolean web;

    @JsonManagedReference
    @OneToMany(mappedBy = "pq", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("fechaCambio DESC")
    private Set<HistorialEstadoPQ> historialEstados;

    @OneToMany(mappedBy = "pq", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AdjuntoPQ> adjuntos;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PreRemove
    public void preRemove() {
        if (adjuntos != null) {
            for (AdjuntoPQ adjunto : adjuntos) {
                adjunto.setDeletedAt(LocalDateTime.now());
            }
        }

        if (historialEstados != null) {
            for (HistorialEstadoPQ historial : historialEstados) {
                historial.setDeletedAt(LocalDateTime.now());
            }
        }
    }
}
