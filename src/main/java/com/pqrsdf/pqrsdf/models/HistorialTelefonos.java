package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "historial_telefonos")
public class HistorialTelefonos extends GenericEntity{
    
    private String telefono;

    @Column(name = "persona_id")
    private Long personaId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected void onCreate(){
        this.updatedAt = LocalDateTime.now();
    }
}
