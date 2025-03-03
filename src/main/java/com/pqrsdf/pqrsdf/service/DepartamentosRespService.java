package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AreasResp;
import com.pqrsdf.pqrsdf.repository.DepartamentosRespRepository;

@Service
public class DepartamentosRespService extends GenericService<AreasResp, Long>{
    public DepartamentosRespService(DepartamentosRespRepository repository){
        super(repository);
    }
}
