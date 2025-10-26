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
@Table(name = "areas_resp")
@SQLDelete(sql = "UPDATE areas_resp SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class AreaResp extends GenericEntity {

    @Column(name = "codigo_dep", nullable = false, length = 10)
    private String codigoDep;

    private String nombre;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
