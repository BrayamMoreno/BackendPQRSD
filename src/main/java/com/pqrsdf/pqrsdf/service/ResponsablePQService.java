package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;
import com.pqrsdf.pqrsdf.repository.ResponsablesPQRepository;

@Service
public class ResponsablePQService extends GenericService<ResponsablePQ, Long>{
    public ResponsablePQService(ResponsablesPQRepository repository){
        super(repository);
    }
}
