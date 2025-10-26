package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "UPDATE personas SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Persona extends GenericEntity {

    private String nombre;

    private String apellido;

    @ManyToOne
    @JoinColumn(name = "tipo_doc")
    private TipoDoc tipoDoc;

    @Column(unique = true, nullable = false)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "tipo_persona")
    private TipoPersona tipoPersona;

    private String telefono;
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    private Boolean tratamientoDatos;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "genero")
    private Genero genero;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Formula("(SELECT u.correo FROM usuarios u WHERE u.persona_id = id LIMIT 1)")
    private String correoUsuario;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
