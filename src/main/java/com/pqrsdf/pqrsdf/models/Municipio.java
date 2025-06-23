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

    @ManyToOne @JoinColumn(name = "departamento_id")
    private Departamento departamento;
}
