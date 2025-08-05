package com.pqrsdf.pqrsdf.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pqrsdf.pqrsdf.generic.GenericRepository;
import com.pqrsdf.pqrsdf.models.HistorialEstadoPQ;

@Repository
public interface HistorialEstadosRespository extends GenericRepository<HistorialEstadoPQ, Long> {
    // Aquí puedes agregar métodos específicos para manejar HistorialEstadoPQ si es necesario.
    List<HistorialEstadoPQ> findByPqId(Long pqId);
}
