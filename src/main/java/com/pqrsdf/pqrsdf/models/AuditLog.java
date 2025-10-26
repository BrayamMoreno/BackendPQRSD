package com.pqrsdf.pqrsdf.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "audit_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String action; // acción lógica realizada (LOGIN, CREAR_BACKUP, etc.)

    private String method; // GET, POST, PUT, DELETE

    private String endpoint; // ruta del endpoint accedido

    private int statusCode; // código de respuesta

    private Timestamp timestamp; // momento de la acción

}
