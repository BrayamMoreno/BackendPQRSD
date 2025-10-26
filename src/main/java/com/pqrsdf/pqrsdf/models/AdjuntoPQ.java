package com.pqrsdf.pqrsdf.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "adjuntos_pq")
@SQLDelete(sql = "UPDATE adjuntos_pq SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class AdjuntoPQ extends GenericEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pq_id", nullable = false)
    private PQ pq;

    @Formula("(select p.numero_radicado from pqs p where p.id = pq_id)")
    private String pqRadicado;

    @Column(nullable = false)
    private String nombreArchivo;

    @Column(nullable = false)
    private String rutaArchivo;

    private boolean respuesta;

    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
