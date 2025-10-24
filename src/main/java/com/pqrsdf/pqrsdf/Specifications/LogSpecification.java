package com.pqrsdf.pqrsdf.Specifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.models.AuditLog;

import jakarta.persistence.criteria.Predicate;

public class LogSpecification {
    public static Specification<AuditLog> hasFechaRango(String fechaInicio, String fechaFin) {
        return (root, query, cb) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd['T'HH:mm:ss]]");

            LocalDateTime inicio = null;
            LocalDateTime fin = null;

            try {
                if (fechaInicio != null && !fechaInicio.isEmpty()) {
                    inicio = LocalDateTime.parse(fechaInicio, formatter);
                }
                if (fechaFin != null && !fechaFin.isEmpty()) {
                    fin = LocalDateTime.parse(fechaFin, formatter);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Formato de fecha inválido. Usa formato ISO 8601: yyyy-MM-dd'T'HH:mm:ss");
            }

            if (inicio == null && fin == null)
                return cb.conjunction();
            if (inicio != null && fin == null)
                return cb.greaterThanOrEqualTo(root.get("timestamp"), inicio);
            if (inicio == null && fin != null)
                return cb.lessThanOrEqualTo(root.get("timestamp"), fin);
            return cb.between(root.get("timestamp"), inicio, fin);
        };
    }

    public static Specification<AuditLog> hasUsuarioOrAccionEndpoitStatusCode(String valor) {
        return (root, query, cb) -> {
            if (valor == null || valor.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + valor.toLowerCase() + "%";

            Predicate porUsuario = cb.like(cb.lower(root.get("username")), likePattern);
            Predicate porAccion = cb.like(cb.lower(root.get("action")), likePattern);
            Predicate porEndpoint = cb.like(cb.lower(root.get("endpoint")), likePattern);
            Predicate porStatusCode = cb.like(cb.lower(cb.toString(root.get("statusCode"))), likePattern);

            return cb.or(
                    porUsuario,
                    porAccion,
                    porEndpoint,
                    porStatusCode
            );
        };
    }
}
