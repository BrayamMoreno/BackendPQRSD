package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tipos_pq")
public class TiposPqs extends GenericEntity{
    
    private String nombre;

    private String descripcion;
}
