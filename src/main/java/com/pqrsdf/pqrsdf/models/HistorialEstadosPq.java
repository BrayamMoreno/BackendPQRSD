package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "historial_estados_pq")
public class HistorialEstadosPq extends GenericEntity{
    
    @Column(name = "pq_iq")
    private Long pqId;

    @Column(name = "estado_anterior_id")
    private String estadoAnteriorId;

    @Column(name = "fecha_cambio")
    private String fechaCambio;

    private String comentario;
}
