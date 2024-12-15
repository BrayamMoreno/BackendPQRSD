package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "departamentos_resp")
public class DepartamentosResp extends GenericEntity{
    
    @Column(name = "codigo_dep")
    private String codigoDep;

    private String nombre;

    private String descripcion;
}
