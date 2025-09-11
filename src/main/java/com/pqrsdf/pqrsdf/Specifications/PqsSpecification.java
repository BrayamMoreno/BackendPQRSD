package com.pqrsdf.pqrsdf.Specifications;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.models.PQ;

public class PqsSpecification {
    public static Specification<PQ> hasTipoId(Long tipoId) {
        return (root, query, criteriaBuilder) -> {
            if (tipoId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("tipoPQ").get("id"), tipoId);
        };
    }

    public static Specification<PQ> hasUltimoEstado(Long estadoId) {
        return (root, query, cb) -> {
            if (estadoId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("ultimoEstadoId"), estadoId);
        };
    }

    public static Specification<PQ> hasSolicitanteId(Long solicitanteId) {
        return (root, query, criteriaBuilder) -> {
            if (solicitanteId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("solicitante").get("id"), solicitanteId);
        };
    }

    public static Specification<PQ> hasNumeroRadicado(String numeroRadicado) {
        return (root, query, criteriaBuilder) -> {
            if (numeroRadicado == null || numeroRadicado.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("numeroRadicado").as(String.class)),
                    "%" + numeroRadicado.toLowerCase() + "%");
        };
    }

    public static Specification<PQ> hasFechaRadicacion(String fechaRadicacion) {
        return (root, query, cb) -> {
            if (fechaRadicacion == null || fechaRadicacion.isEmpty()) {
                return cb.conjunction();
            }

            LocalDate fecha = LocalDate.parse(fechaRadicacion);
            Date sqlDate = java.sql.Date.valueOf(fecha);

            return cb.equal(root.get("fechaRadicacion"), sqlDate);
        };
    }

    public static Specification<PQ> hasResponsableId(Long responsableId) {
        return (root, query, criteriaBuilder) -> {
            if (responsableId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("responsable").get("id"), responsableId);
        };
    }
}
