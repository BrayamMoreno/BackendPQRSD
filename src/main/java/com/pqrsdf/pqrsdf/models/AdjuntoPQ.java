package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
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
@Table(name = "adjuntos_pq")
public class AdjuntoPQ extends GenericEntity{
 
    @ManyToOne
    @JoinColumn(name = "pq_id", nullable = false)
    private PQ pq;

    @Column(nullable = false)
    private String nombreArchivo;

    @Column(nullable = false)
    private String rutaArchivo;

    private Timestamp createdAt;

    @PrePersist protected void onCreate(){
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

}
