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
@Table(name = "seguimientos_pq")
public class SeguimientosPq extends GenericEntity{

    @Column(name = "pq_id")
    private Long pqId;

    @Column(name = "responsable_pq_id")
    private Long responsablePqId;

    private String comentario;

    @Column(name = "estado_id")
    private String estadoId;

    private boolean activo;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;
}
