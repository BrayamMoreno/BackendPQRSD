package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Departamentos;
import com.pqrsdf.pqrsdf.repository.DepartamentosRepositry;

@Service
public class DepartamenttosService extends GenericService<Departamentos, Long>{
    public DepartamenttosService(DepartamentosRepositry repositry){
        super(repositry);
    }
}
