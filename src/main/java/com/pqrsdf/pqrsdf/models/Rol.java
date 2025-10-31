package com.pqrsdf.pqrsdf.models;

import java.util.Set;

import org.hibernate.Hibernate;

import java.util.Date;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "roles")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Rol extends GenericEntity {

    @Column(unique = true, nullable = false, length = 20)
    private String nombre;

    @Column(length = 256)
    private String descripcion;

    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_permisos", joinColumns = @JoinColumn(name = "rol_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    @JsonIgnore
    private Set<Permiso> permisos = new HashSet<>();

    @Transient
    private boolean exposePermisos = false;

    @JsonProperty("permisos")
    public Set<Permiso> getPermisosVisible() {
        if (exposePermisos) {
            Hibernate.initialize(permisos);
            return permisos;
        }
        return null;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
