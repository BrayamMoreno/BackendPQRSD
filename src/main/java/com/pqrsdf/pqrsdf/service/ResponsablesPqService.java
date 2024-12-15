package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.ResponsablesPq;
import com.pqrsdf.pqrsdf.repository.ResponsablesPqRepository;

@Service
public class ResponsablesPqService extends GenericService<ResponsablesPq, Long>{
    public ResponsablesPqService(ResponsablesPqRepository repository){
        super(repository);
    }
}
