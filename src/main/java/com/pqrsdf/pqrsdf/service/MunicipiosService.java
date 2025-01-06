package com.pqrsdf.pqrsdf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Municipios;
import com.pqrsdf.pqrsdf.repository.MunicipiosRepository;

@Service
public class MunicipiosService extends GenericService<Municipios, Long>{

    private final MunicipiosRepository repository;

    public MunicipiosService(MunicipiosRepository repository){
        super(repository);
        this.repository = repository;
    }

    public List<Municipios> findByDepartamentoId(Long departamentoId){
        return repository.findByDepartamentoId(departamentoId);
    }
}
