package com.pqrsdf.pqrsdf.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pqrsdf.pqrsdf.models.ConsecutivoPq;
import com.pqrsdf.pqrsdf.repository.ConsecutivoPqRepository;

import jakarta.transaction.Transactional;

@Service
public class IdentificadorPqService {

    @Autowired
    private ConsecutivoPqRepository consecutivoPqRepository;

    @Transactional
    public String generarIdentificadorPq() {
        int yearActual = LocalDate.now().getYear();

        // Buscar el registro del año actual o crearlo si no existe
        ConsecutivoPq consecutivo = consecutivoPqRepository.findByYear(yearActual);

        if (consecutivo == null) {
            consecutivo = new ConsecutivoPq();
            consecutivo.setYear(yearActual);
            consecutivo.setUltimoConsecutivo(0);
        }

        // Incrementar el consecutivo y guardar
        int nuevoConsecutivo = consecutivo.getUltimoConsecutivo() + 1;
        consecutivo.setUltimoConsecutivo(nuevoConsecutivo);
        consecutivoPqRepository.save(consecutivo);

        // Formatear el identificador
        return String.format("PQ-%d-%05d", yearActual, nuevoConsecutivo);
    }
}
