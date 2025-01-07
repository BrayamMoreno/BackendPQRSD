package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "correos_pq")
public class CorreosPq extends GenericEntity {
    
    @Column(name = "pq_id")
    private Long pqId;
    
    private String correo;

}
