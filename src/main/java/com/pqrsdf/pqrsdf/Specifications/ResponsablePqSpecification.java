package com.pqrsdf.pqrsdf.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.models.AreaResp;
import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.ResponsablePQ;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ResponsablePqSpecification {
    public static Specification<ResponsablePQ> hasArea(Long areaId) {
        return (root, query, criteriaBuilder) -> {
            if (areaId == null) {
                return criteriaBuilder.conjunction();
            }

            Join<ResponsablePQ, AreaResp> areaJoin = root.join("area", JoinType.LEFT);

            return criteriaBuilder.equal(areaJoin.get("id"), areaId);
        };
    }

    public static Specification<ResponsablePQ> hasName(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + search.toLowerCase() + "%";

            Join<ResponsablePQ, Persona> personaJoin = root.join("personaResponsable", JoinType.LEFT);

            Predicate porNombre = criteriaBuilder.like(criteriaBuilder.lower(personaJoin.get("nombre")), likePattern);
              Predicate porApellido = criteriaBuilder.like(criteriaBuilder.lower(personaJoin.get("apellido")), likePattern);

            return criteriaBuilder.or(porNombre, porApellido);
        };
    }
}
