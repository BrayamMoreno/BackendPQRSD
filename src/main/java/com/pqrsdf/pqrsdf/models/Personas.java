package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "personas")
public class Personas extends GenericEntity{
    
    private String nombre;

    private String apellido;

    @Column(name = "tipo_doc")
    private Long tipoDoc;

    private String dni;

    @Column(name = "tipo_persona")
    private Long tipoPersona;

    private String telefono;

    private String direccion;

    @Column(name = "municipio_id")
    private Long municipioId;

    private Long genero;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private boolean anonimo;

    private boolean activo;

    @PrePersist protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}


