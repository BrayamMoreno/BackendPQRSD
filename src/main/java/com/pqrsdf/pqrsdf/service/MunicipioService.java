package com.pqrsdf.pqrsdf.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Municipio;
import com.pqrsdf.pqrsdf.repository.MunicipioRepository;

@Service
public class MunicipioService extends GenericService<Municipio, Long> {

    private final MunicipioRepository repository;

    public MunicipioService(MunicipioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Page<Municipio> findByDepartamentoId(Long departamentoId, Pageable pageable) {
        return repository.findByDepartamentoId(departamentoId, pageable);
    }

}
