package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "radicador_pq")
public class RadicadorPq extends GenericEntity{
    
    @Column(name = "persona_id")
    private Long personaId;

    @Column(name = "codigo_radicador")
    private Long codigoRadicador;
}
