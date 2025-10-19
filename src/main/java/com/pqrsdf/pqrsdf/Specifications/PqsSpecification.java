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

    public static Specification<PQ> hasFechaRango(String fechaInicio, String fechaFin) {
        return (root, query, cb) -> {
            if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty())) {
                return cb.conjunction();
            }

            if (fechaInicio != null && !fechaInicio.isEmpty() && (fechaFin == null || fechaFin.isEmpty())) {
                LocalDate inicio = LocalDate.parse(fechaInicio);
                Date sqlFechaInicio = java.sql.Date.valueOf(inicio);
                return cb.greaterThanOrEqualTo(root.get("fechaRadicacion"), sqlFechaInicio);
            }

            if ((fechaInicio == null || fechaInicio.isEmpty()) && fechaFin != null && !fechaFin.isEmpty()) {
                LocalDate fin = LocalDate.parse(fechaFin);
                Date sqlFechaFin = java.sql.Date.valueOf(fin);
                return cb.lessThanOrEqualTo(root.get("fechaRadicacion"), sqlFechaFin);
            }

            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            Date sqlFechaInicio = java.sql.Date.valueOf(inicio);
            Date sqlFechaFin = java.sql.Date.valueOf(fin);

            return cb.between(root.get("fechaRadicacion"), sqlFechaInicio, sqlFechaFin);
        };
    }

    public static Specification<PQ> hasRadicadorId(Long radicadorId) {
        return (root, query, criteriaBuilder) -> {
            if (radicadorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("radicador").get("id"), radicadorId);
        };
    }

    public static Specification<PQ> hasResponsableNull() {
        return (root, query, cb) -> cb.isNull(root.get("responsable"));
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
