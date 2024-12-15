package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "historial_seguimientos_pq")
public class HistorialSeguimientosPq extends GenericEntity{

    @Column(name = "pq_id")
    private Long pqId;

    @Column(name = "estado_anterior_id")
    private Long estadoAnteriorId;

    @Column(name = "responsable_anterior_id")
    private Long responsableAnteriorId;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;

    @Column(name = "comentario_anterior")
    private String comentarioAnterior;
}
