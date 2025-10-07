package com.pqrsdf.pqrsdf.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.pqrsdf.pqrsdf.models.Persona;
import com.pqrsdf.pqrsdf.models.Rol;
import com.pqrsdf.pqrsdf.models.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class UsuarioSpecification {

    public static Specification<Usuario> hasNombreOrApellidoorCorreo(String valor) {
        return (root, query, criteriaBuilder) -> {
            if (valor == null || valor.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + valor.toLowerCase() + "%";

            Join<Usuario, Persona> personaJoin = root.join("persona", JoinType.LEFT);

            Predicate porNombre = criteriaBuilder.like(criteriaBuilder.lower(personaJoin.get("nombre")), likePattern);
            Predicate porApellido = criteriaBuilder.like(criteriaBuilder.lower(personaJoin.get("apellido")), likePattern);
            Predicate porCorreo = criteriaBuilder.like(criteriaBuilder.lower(root.get("correo")), likePattern);

            // busca coincidencia en cualquiera de los tres campos
            return criteriaBuilder.or(porNombre, porApellido, porCorreo);
        };
    }

    public static Specification<Usuario> hasRolId(Long rolId) {
        return (root, query, criteriaBuilder) -> {
            if (rolId == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Usuario, Rol> rolJoin = root.join("rol", JoinType.LEFT);

            return criteriaBuilder.equal(rolJoin.get("id"), rolId);
        };
    }

    public static Specification<Usuario> hasEstado(Boolean estado) {
        return (root, query, criteriaBuilder) -> {
            if (estado == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("isEnable"), estado);
        };
    }

}


