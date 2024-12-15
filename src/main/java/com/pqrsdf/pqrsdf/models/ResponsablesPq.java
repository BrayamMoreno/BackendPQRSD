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
@Table(name = "responsables_pq")
public class ResponsablesPq extends GenericEntity{
    
    @Column(name = "departamento_id")
    private long departamentoId;

    @Column(name = "usuario_responsable_id")
    private Long usuarioResponsableId;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

}
