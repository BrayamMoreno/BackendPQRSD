package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "personas")
public class Personas extends GenericEntity{
    
    @Column(length = 64, nullable = false)
    private String nombre;

    @Column(length = 64, nullable = false)
    private String apellido;

    @Column(name = "tipo_doc", nullable = false)
    private Long tipoDoc;

    private String dni;

    @Column(name = "tipo_persona", nullable = false)
    private Long tipoPersona;

    private String telefono;

    private String direccion;

    @Column(name = "codigo_radicador", length = 5)
    private String codigoRadicador;

    @Column(nullable = false)
    private boolean tratamiento_datos;

    @Column(name = "municipio_id")
    private Long municipioId;

    @Column(nullable = false)
    private Long genero;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}


