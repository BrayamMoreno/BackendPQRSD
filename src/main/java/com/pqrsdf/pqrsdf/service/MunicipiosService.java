package com.pqrsdf.pqrsdf.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Municipios;
import com.pqrsdf.pqrsdf.repository.MunicipiosRepository;

@Service
public class MunicipiosService extends GenericService<Municipios, Long> {

    private final MunicipiosRepository repository;

    public MunicipiosService(MunicipiosRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Page<Municipios> findByDepartamentoId(Long departamentoId, Pageable pageable) {
        return repository.findByDepartamentoId(departamentoId, pageable);
    }

}
