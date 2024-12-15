package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "estados_pq")
public class EstadosPq extends GenericEntity{
    
    private String nombre;

    private String color;
}
