package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class HistorialEstadoPQ extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "pq_id", nullable = false)
    private PQ pq;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoPQ estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String observacion;
    private Timestamp fechaCambio;
}