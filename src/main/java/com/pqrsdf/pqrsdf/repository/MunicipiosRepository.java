package com.pqrsdf.pqrsdf.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Municipios;

@Repository
public interface MunicipiosRepository extends GenericRepository<Municipios, Long>{
    List<Municipios> findByDepartamentoId(Long departamentoId);
}
