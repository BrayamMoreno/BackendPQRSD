package com.pqrsdf.pqrsdf.models;

import java.util.Set;
import java.util.Date;
import java.util.HashSet;

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
public class Rol extends GenericEntity {

    @Column(unique = true, nullable = false, length = 20)
    private String nombre;

    @Column(length = 256)
    private String descripcion;

    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permisos", joinColumns = @JoinColumn(name = "rol_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    private Set<Permiso> permisos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
