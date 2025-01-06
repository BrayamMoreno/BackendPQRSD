package com.pqrsdf.pqrsdf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.Municipios;

@Repository
public interface MunicipiosRepository extends GenericRepository<Municipios, Long>{
    Page<Municipios> findByDepartamentoId(Long departamentoId, Pageable pageable);

}
