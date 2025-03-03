package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "departamentos")
public class Departamentos extends GenericEntity{
    
    @Column(length = 64)
    private String nombre;

    @Column(name = "codigo_dane", length = 10)
    private String codigoDane;
}
