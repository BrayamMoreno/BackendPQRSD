package com.pqrsdf.pqrsdf.service;

import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.generic.GenericService;
import com.pqrsdf.pqrsdf.models.Departamento;
import com.pqrsdf.pqrsdf.repository.DepartamentoRepositry;

@Service
public class DepartamentoService extends GenericService<Departamento, Long>{
    public DepartamentoService(DepartamentoRepositry repositry){
        super(repositry);
    }
}
