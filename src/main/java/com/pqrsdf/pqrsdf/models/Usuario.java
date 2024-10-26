package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import io.swagger.v3.oas.annotations.Hidden;

import jakarta.persistence.*;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Usuarios")
public class Usuario extends GenericEntity {
    
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_enable")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "persona_id")
    private Long persona_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Column(name = "fecha_ultima_modificacion")
    @Hidden
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "fecha_registro")
    @Hidden
    private LocalDateTime fechaRegistro;

    @PrePersist protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    @PreUpdate protected void onUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }
}
