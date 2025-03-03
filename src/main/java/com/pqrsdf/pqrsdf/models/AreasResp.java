package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "area_resp")
public class AreasResp extends GenericEntity{
    
    @Column(name = "codigo_area", length = 10, nullable = false)
    private String codigoArea;

    @Column(length = 64, nullable = false)
    private String nombre;
}
