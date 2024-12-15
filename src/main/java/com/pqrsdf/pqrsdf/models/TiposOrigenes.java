package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tipos_origenes")
public class TiposOrigenes extends GenericEntity{
    
    @Column(nullable = false, length = 64)
    private String name;
}
