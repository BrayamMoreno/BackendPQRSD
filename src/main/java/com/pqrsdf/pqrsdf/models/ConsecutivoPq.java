package com.pqrsdf.pqrsdf.models;

import com.pqrsdf.pqrsdf.generic.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "consecutivo_pqs")
public class ConsecutivoPq extends GenericEntity {
    
    @Column(nullable = false, unique = true)
    private int year;

    @Column(nullable = false)
    private int ultimoConsecutivo;

    public ConsecutivoPq orElseGet(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseGet'");
    }

    // Getters y setters
}
