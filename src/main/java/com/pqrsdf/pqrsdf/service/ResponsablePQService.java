package com.pqrsdf.pqrsdf.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;
import com.pqrsdf.pqrsdf.repository.ResponsablePQRepository;

@Service
public class ResponsablePQService extends GenericService<ResponsablePQ, Long>{

    private final ResponsablePQRepository repository;

    public ResponsablePQService(ResponsablePQRepository repository){
        super(repository);
        this.repository = repository;
    }

    public Page<ResponsablePQ> findAll(Specification<ResponsablePQ> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }
}
