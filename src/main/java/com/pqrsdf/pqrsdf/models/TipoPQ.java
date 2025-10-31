package com.pqrsdf.pqrsdf.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tipos_pq")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoPQ extends GenericEntity {

    private String nombre;

    @Column(name = "dias_habiles_respuesta", nullable = false)
    private Long diasHabilesRespuesta;
}
