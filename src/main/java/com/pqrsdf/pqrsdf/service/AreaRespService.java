package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.AreaResp;
import com.pqrsdf.pqrsdf.repository.AreaRespRepository;

@Service
public class AreaRespService extends GenericService<AreaResp, Long>{
    public AreaRespService(AreaRespRepository repository){
        super(repository);
    }
}
