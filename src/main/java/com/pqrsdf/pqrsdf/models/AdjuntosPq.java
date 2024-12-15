package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import io.swagger.v3.oas.annotations.Hidden;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "adjuntos_pq")
public class AdjuntosPq extends GenericEntity{
 
    @Column(name = "pq_id")
    private Long pqId;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Hidden
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist protected void onCreate(){
        this.createAt = LocalDateTime.now();
    }

}
