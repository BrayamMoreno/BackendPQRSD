package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "permisos")
@Schema(description = "Modelo de Permisos")
public class Permiso extends GenericEntity {

    @Column(unique = true, nullable = false, length = 20)
    private String tabla;

    private Boolean agregar;
    private Boolean modificar;
    private Boolean eliminar;
    private Boolean leer;
}

