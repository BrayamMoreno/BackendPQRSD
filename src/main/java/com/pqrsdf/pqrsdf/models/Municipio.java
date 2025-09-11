package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "municipios")
public class Municipio extends GenericEntity {

    private String nombre;
    private String codigoDane;

    @Column(name = "departamento_id", nullable = false)
    private Long departamentoId;
}
