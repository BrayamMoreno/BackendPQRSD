package com.pqrsdf.pqrsdf.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.models.AdjuntoPQ;

public class AdjuntosSpecification {

    public static Specification<AdjuntoPQ> hasRespuesta(Boolean respuesta) {
        return (root, query, criteriaBuilder) -> {
            if (respuesta == null) {
                return criteriaBuilder.conjunction(); // No filtra nada
            }
            return criteriaBuilder.equal(root.get("respuesta"), respuesta);
        };
    }

    public static Specification<AdjuntoPQ> hasNombreArchivoOrPqRadicado(String valor) {
    return (root, query, criteriaBuilder) -> {
        if (valor == null || valor.isEmpty()) {
            return criteriaBuilder.conjunction(); // No aplica ningún filtro
        }

        String likePattern = "%" + valor.toLowerCase() + "%";

        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreArchivo")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("pqRadicado")), likePattern)
        );
    };
}


}
