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
public class Municipios extends GenericEntity{

    private String nombre;

    @Column(name = "codigo_dane")
    private String codigoDane;

    @Column(name = "departamento_id")
    private Long departamentoId;
}
