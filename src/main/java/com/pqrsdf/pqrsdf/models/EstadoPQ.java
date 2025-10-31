package com.pqrsdf.pqrsdf.models;

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
@Table(name = "estados_pq")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoPQ extends GenericEntity {

    private String nombre;

    private String color;

    private String descripcion;
}
