package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Municipios;
import com.pqrsdf.pqrsdf.repository.MunicipiosRepository;

@Service
public class MunicipiosService extends GenericService<Municipios, Long>{
    public MunicipiosService(MunicipiosRepository repository){
        super(repository);
    }
}
