package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class HistorialEstadoPQ extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "pq_id", nullable = false)
    @JsonBackReference
    private PQ pq;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPQ estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

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
