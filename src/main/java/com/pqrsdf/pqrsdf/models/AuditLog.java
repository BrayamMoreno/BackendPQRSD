package com.pqrsdf.pqrsdf.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Type;
import java.time.LocalDateTime;

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
    private String method;
    private String endpoint;
    private int statusCode;
    private LocalDateTime timestamp;

    @Type(JsonBinaryType.class) // ✅ forma correcta en Hibernate 6
    @Column(columnDefinition = "jsonb")
    private Object requestBody;

    @Type(JsonBinaryType.class) // ✅ igual aquí
    @Column(columnDefinition = "jsonb")
    private Object responseBody;

}
