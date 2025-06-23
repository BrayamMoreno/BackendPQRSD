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
public class ResponsablePQ extends GenericEntity {

    @ManyToOne @JoinColumn(name = "persona_responsable_id")
    private Persona personaResponsable;

    @ManyToOne @JoinColumn(name = "area_id")
    private AreaResp area;

    private LocalDateTime fechaAsignacion;
}
