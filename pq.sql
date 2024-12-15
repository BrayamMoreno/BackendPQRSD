CREATE TABLE "permisos" (
  "id" serial PRIMARY KEY,
  "tabla" varchar(20) UNIQUE NOT NULL,
  "agregar" boolean,
  "modificar" boolean,
  "eliminar" boolean,
  "leer" boolean,
  "opc1" boolean,
  "opc2" boolean
);

CREATE TABLE "roles" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(20) UNIQUE NOT NULL
);

CREATE TABLE "roles_permisos" (
  "id" serial PRIMARY KEY,
  "rol_id" integer,
  "permiso_id" integer
);

CREATE TABLE "departamentos" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64),
  "codigo_dane" varchar(10)
);

CREATE TABLE "municipios" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64),
  "codigo_dane" varchar(10),
  "departamento_id" integer
);

CREATE TABLE "tipos_docs" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64)
);

CREATE TABLE "tipos_personas" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(32)
);

CREATE TABLE "personas" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64) NOT NULL,
  "apellido" varchar(64) NOT NULL,
  "tipo_doc" integer,
  "dni" varchar UNIQUE NOT NULL,
  "tipo_persona" integer,
  "telefono" varchar(16),
  "email" varchar(64),
  "direccion" varchar(126),
  "municipio_id" integer,
  "genero" integer,
  "create_at" timestamp,
  "update_at" timestamp,
  "anonimo" boolean,
  "activo" boolean
);

CREATE TABLE "usuarios" (
  "id" serial PRIMARY KEY,
  "username" varchar(50) UNIQUE NOT NULL,
  "password" varchar(200) NOT NULL,
  "is_enable" boolean DEFAULT true,
  "account_no_expired" boolean DEFAULT true,
  "account_no_locked" boolean DEFAULT true,
  "credential_no_expired" boolean DEFAULT true,
  "politica_privacidad_aceptada" boolean DEFAULT true,
  "fecha_aceptacion_politica" timestamp,
  "reset_token" varchar(100),
  "persona_id" integer,
  "rol_id" integer,
  "create_at" timestamp,
  "update_at" timestamp
);

CREATE TABLE "tipos_pq" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64),
  "descripcion" varchar(256)
);

CREATE TABLE "estados_pq" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64),
  "color" varchar(64)
);

CREATE TABLE "pqs" (
  "id" serial PRIMARY KEY,
  "identificador" varchar(20) UNIQUE NOT NULL,
  "numero_radicado" varchar,
  "detalle_asunto" text,
  "tipo_pq_id" integer,
  "solicitante_id" integer,
  "numero_folio" integer,
  "fecha_radicacion" date,
  "hora_radicacion" time,
  "fecha_resolucion_estimada" timestamp,
  "fecha_resolucion" timestamp,
  "respuesta" text,
  "radicador_id" integer,
  "modificador_id" integer
);

CREATE TABLE "seguimientos_pq" (
  "id" serial PRIMARY KEY,
  "pq_id" integer,
  "responsable_actual_id" integer,
  "comentario" text,
  "estado_id" integer,
  "fecha_creacion" timestamp NOT NULL,
  "fecha_modificacion" timestamp
);

CREATE TABLE "adjuntos_pq" (
  "id" serial PRIMARY KEY,
  "pq_id" integer,
  "nombre_archivo" varchar(128),
  "ruta_archivo" varchar(128),
  "create_at" timestamp
);

CREATE TABLE "departamentos_resp" (
  "id" serial PRIMARY KEY,
  "codigo_dep" varchar(10),
  "nombre" varchar(64),
  "descripcion" varchar(256)
);

CREATE TABLE "responsables_pq" (
  "id" serial PRIMARY KEY,
  "departamento_id" integer,
  "usuario_responsable_id" integer,
  "fecha_asignacion" timestamp
);

CREATE TABLE "radicador_pq" (
  "id" serial PRIMARY KEY,
  "persona_id" integer,
  "codigo_radicador" integer
);

CREATE TABLE "historial_estados_pq" (
  "id" serial PRIMARY KEY,
  "pq_id" integer,
  "estado_anterior_id" integer,
  "fecha_cambio" timestamp NOT NULL,
  "comentario" text
);

CREATE TABLE "historial_seguimientos_pq" (
  "id" serial PRIMARY KEY,
  "pq_id" integer,
  "estado_anterior_id" integer,
  "responsable_anterior_id" integer,
  "comentario_anterior" text,
  "fecha_cambio" timestamp NOT NULL,
  "comentario_cambio" text
);

COMMENT ON TABLE "usuarios" IS 'ppa = politica_privacidad_aceptada p';

ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");

ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("permiso_id") REFERENCES "permisos" ("id");

ALTER TABLE "municipios" ADD FOREIGN KEY ("departamento_id") REFERENCES "departamentos" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_doc") REFERENCES "tipos_docs" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_persona") REFERENCES "tipos_personas" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("municipio_id") REFERENCES "municipios" ("id");

ALTER TABLE "usuarios" ADD FOREIGN KEY ("persona_id") REFERENCES "personas" ("id");

ALTER TABLE "usuarios" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("tipo_pq_id") REFERENCES "tipos_pq" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("solicitante_id") REFERENCES "personas" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("radicador_id") REFERENCES "personas" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("modificador_id") REFERENCES "usuarios" ("id");

ALTER TABLE "seguimientos_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");

ALTER TABLE "seguimientos_pq" ADD FOREIGN KEY ("responsable_actual_id") REFERENCES "responsables_pq" ("id");

ALTER TABLE "seguimientos_pq" ADD FOREIGN KEY ("estado_id") REFERENCES "estados_pq" ("id");

ALTER TABLE "adjuntos_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");

ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("departamento_id") REFERENCES "departamentos_resp" ("id");

ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("usuario_responsable_id") REFERENCES "personas" ("id");

ALTER TABLE "radicador_pq" ADD FOREIGN KEY ("persona_id") REFERENCES "personas" ("id");

ALTER TABLE "historial_estados_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");

ALTER TABLE "historial_estados_pq" ADD FOREIGN KEY ("estado_anterior_id") REFERENCES "estados_pq" ("id");

ALTER TABLE "historial_seguimientos_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");

ALTER TABLE "historial_seguimientos_pq" ADD FOREIGN KEY ("estado_anterior_id") REFERENCES "estados_pq" ("id");

ALTER TABLE "historial_seguimientos_pq" ADD FOREIGN KEY ("responsable_anterior_id") REFERENCES "responsables_pq" ("id");

CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION(SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION(EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION(PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);
