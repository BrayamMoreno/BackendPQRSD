package com.pqrsdf.pqrsdf.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permiso extends GenericEntity {

    @Column(nullable = false, length = 100)
    private String tabla;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
