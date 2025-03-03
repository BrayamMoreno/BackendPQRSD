package com.pqrsdf.pqrsdf.generic;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

public abstract class GenericService<T,ID> {
    
    private final GenericRepository<T,ID> repository;

    public GenericService(GenericRepository<T,ID> repository){
        this.repository = repository;
    }

    public Page<T> getAllEntitires(Pageable pageable){
        return repository.findAll(pageable);
    }

    public T getById(ID id){
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public T createEntity(T entity){
        return repository.save(entity);
    }

    public void deleteById(ID id){
        repository.deleteById(id);
    }

    public T updateEntity(T existingEntity, T newEntity){
        BeanUtils.copyProperties(existingEntity, newEntity, "Id");
        return repository.save(existingEntity);
    }

    public boolean isPresent(ID id){
        return repository.existsById(id);
    }
}
