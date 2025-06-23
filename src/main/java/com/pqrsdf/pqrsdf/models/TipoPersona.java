package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tipos_personas")
public class TipoPersona extends GenericEntity{

    @Column(name = "nombre", unique = true, length = 64)
    private String nombre;
}
