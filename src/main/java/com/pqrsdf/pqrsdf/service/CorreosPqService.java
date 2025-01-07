package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.CorreosPq;
import com.pqrsdf.pqrsdf.repository.CorreosPqRepository;

@Service
public class CorreosPqService extends GenericService<CorreosPq, Long> {
    public CorreosPqService(CorreosPqRepository repository) {
        super(repository);
    }
}
