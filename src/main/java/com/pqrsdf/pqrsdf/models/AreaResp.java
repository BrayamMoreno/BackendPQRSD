package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "areas_resp")
public class AreaResp extends GenericEntity{

    @Column(name = "codigo_dep", nullable = false, length = 10)
    private String codigoDep;

    private String nombre;

}
