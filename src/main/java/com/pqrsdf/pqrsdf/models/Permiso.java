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

    @Column(nullable = false)
    private String tabla;

    private boolean agregar;

    @Column(nullable = false)
    private boolean modificar;

    @Column(nullable = false)
    private boolean eliminar;

    @Column(nullable = false)
    private boolean leer;
}
