package com.pqrsdf.pqrsdf.models;

import java.util.List;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "departamentos")
public class Departamento extends GenericEntity {

    private String nombre;
    private String codigoDane;

    @OneToMany(mappedBy = "departamento")
    private List<Municipio> municipios;
}
