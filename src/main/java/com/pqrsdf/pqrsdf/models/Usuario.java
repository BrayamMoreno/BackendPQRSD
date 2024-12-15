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
    private Roles rol;

    @Column(name = "update_at")
    @Hidden
    private LocalDateTime update_at;

    @Column(name = "create_at")
    @Hidden
    private LocalDateTime create_at;

    @PrePersist protected void onCreate() {
        this.create_at = LocalDateTime.now();
    }
    
    @PreUpdate protected void onUpdate() {
        this.update_at = LocalDateTime.now();
    }
}
