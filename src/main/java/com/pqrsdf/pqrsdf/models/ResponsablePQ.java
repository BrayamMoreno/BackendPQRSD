package com.pqrsdf.pqrsdf.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "responsables_pq")
@SQLDelete(sql = "UPDATE responsables_pq SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class ResponsablePQ extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "persona_responsable_id")
    private Persona personaResponsable;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AreaResp area;

    private LocalDateTime fechaAsignacion;

    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        if (this.fechaAsignacion == null) {
            this.fechaAsignacion = LocalDateTime.now();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
