package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HistorialEstadoPQ extends GenericEntity {

    @ManyToOne
    @JsonIgnore
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