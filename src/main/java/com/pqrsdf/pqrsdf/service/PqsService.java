package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Pqs;
import com.pqrsdf.pqrsdf.repository.PqsRepository;

@Service
public class PqsService extends GenericService<Pqs, Long>{
    public PqsService(PqsRepository repository){
        super(repository);
    }
}
