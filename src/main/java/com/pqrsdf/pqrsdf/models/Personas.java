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
    private String tipoPersona;

    private String telefono;

    private String email;

    private String direccion;

    @Column(name = "municipio_id")
    private Long municipioId;

    private Long genero;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    private boolean anonimo;

    private boolean activo;

    @PrePersist protected void onCreate(){
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate protected void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }
}


