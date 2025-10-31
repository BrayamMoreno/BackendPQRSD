package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "historial_estados_pq")
@SQLDelete(sql = "UPDATE historial_estados_pq SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HistorialEstadoPQ extends GenericEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pq_id", nullable = false)
    @JsonBackReference
    private PQ pq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPQ estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cambiado_por_id", nullable = false)
    private Usuario cambiadoPor;

    private String observacion;
    private Timestamp fechaCambio;

    @Formula("(select p.numero_radicado from pqs p where p.id = pq_id)")
    private String numeroRadicado;

    @PrePersist
    protected void onCreate() {
        this.fechaCambio = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
