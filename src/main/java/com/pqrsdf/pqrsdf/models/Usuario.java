package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario extends GenericEntity {
    
    @Column(length = 64, nullable = false)
    private String correo;

    @Column(length = 200, nullable = false)
    private String contrasena;

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
    private Roles rol;

    @Column(name = "updated_at")
    @Hidden
    private LocalDateTime updated_at;

    @Column(name = "created_at")
    @Hidden
    private LocalDateTime created_at;

    @PrePersist protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }
    
    @PreUpdate protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
