CREATE TABLE "permisos" (
  "id" serial PRIMARY KEY,
  "tabla" varchar(20) UNIQUE NOT NULL,
  "agregar" boolean,
  "modificar" boolean,
  "eliminar" boolean,
  "leer" boolean
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
  "departamento_id" integer NOT NULL
);

CREATE TABLE "tipos_docs" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64) NOT NULL
);

CREATE TABLE "tipos_personas" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(32) NOT NULL
);

CREATE TABLE "generos" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(16) NOT NULL
);

CREATE TABLE "personas" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64) NOT NULL,
  "apellido" varchar(64) NOT NULL,
  "tipo_doc" integer,
  "dni" varchar(20) UNIQUE NOT NULL,
  "tipo_persona" integer,
  "telefono" varchar(15) NOT NULL,
  "direccion" varchar(126),
  "codigo_radicador" varchar(5),
  "tratamiento_datos" boolean NOT NULL,
  "municipio_id" integer,
  "genero" integer NOT NULL,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE TABLE "usuarios" (
  "id" serial PRIMARY KEY,
  "correo" varchar(64) NOT NULL,
  "contraseña" varchar(200) NOT NULL,
  "is_enable" boolean DEFAULT true,
  "account_no_expired" boolean DEFAULT true,
  "account_no_locked" boolean DEFAULT true,
  "credential_no_expired" boolean DEFAULT true,
  "reset_token" varchar(100),
  "persona_id" integer,
  "rol_id" integer,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE TABLE "tipos_pq" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64) NOT NULL
);

CREATE TABLE "estados_pq" (
  "id" serial PRIMARY KEY,
  "nombre" varchar(64) NOT NULL
);

CREATE TABLE "pqs" (
  "id" serial PRIMARY KEY,
  "consecutivo" varchar(20) UNIQUE NOT NULL,
  "numero_radicado" varchar UNIQUE NOT NULL,
  "numero_folio" integer,
  "detalle_asunto" text NOT NULL,
  "tipo_pq_id" integer,
  "solicitante_id" integer,
  "fecha_radicacion" date,
  "hora_radicacion" time,
  "fecha_resolucion_estimada" date,
  "fecha_resolucion" date,
  "radicador_id" integer,
  "responsable_id" integer,
  "web" boolean NOT NULL
);

CREATE TABLE "adjuntos_pq" (
  "id" serial PRIMARY KEY,
  "pq_id" integer NOT NULL,
  "nombre_archivo" varchar(128) NOT NULL,
  "ruta_archivo" varchar(128) NOT NULL,
  "created_at" timestamp
);

CREATE TABLE "areas_resp" (
  "id" serial PRIMARY KEY,
  "codigo_dep" varchar(10) NOT NULL,
  "nombre" varchar(64) NOT NULL
);

CREATE TABLE "responsables_pq" (
  "id" serial PRIMARY KEY,
  "persona_responsable_id" integer,
  "area_id" integer,
  "fecha_asignacion" timestamp
);

CREATE INDEX "index_codigo_radicador" ON "personas" ("codigo_radicador");

CREATE INDEX "index_dni" ON "personas" ("dni");

CREATE INDEX "correo_username" ON "usuarios" ("correo");

CREATE INDEX "Index_consecutivo" ON "pqs" ("consecutivo");

CREATE INDEX "Index_numero_radicado" ON "pqs" ("numero_radicado");

CREATE INDEX "index_area_id" ON "responsables_pq" ("area_id");

ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");

ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("permiso_id") REFERENCES "permisos" ("id");

ALTER TABLE "municipios" ADD FOREIGN KEY ("departamento_id") REFERENCES "departamentos" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_doc") REFERENCES "tipos_docs" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_persona") REFERENCES "tipos_personas" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("municipio_id") REFERENCES "municipios" ("id");

ALTER TABLE "personas" ADD FOREIGN KEY ("genero") REFERENCES "generos" ("id");

ALTER TABLE "usuarios" ADD FOREIGN KEY ("persona_id") REFERENCES "personas" ("id");

ALTER TABLE "usuarios" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("tipo_pq_id") REFERENCES "tipos_pq" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("solicitante_id") REFERENCES "personas" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("radicador_id") REFERENCES "personas" ("id");

ALTER TABLE "pqs" ADD FOREIGN KEY ("responsable_id") REFERENCES "responsables_pq" ("id");

ALTER TABLE "adjuntos_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");

ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("persona_responsable_id") REFERENCES "personas" ("id");

ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("area_id") REFERENCES "areas_resp" ("id");

CREATE TABLE spring_session (
  primary_id VARCHAR(36) NOT NULL,          -- ID interno único generado por Spring Session.
  session_id VARCHAR(36) NOT NULL,         -- ID público de la sesión (visible en las cookies del cliente).
  creation_time BIGINT NOT NULL,           -- Marca de tiempo UNIX para cuando se creó la sesión.
  last_access_time BIGINT NOT NULL,        -- Marca de tiempo UNIX para el último acceso.
  max_inactive_interval INT NOT NULL,      -- Intervalo máximo de inactividad (en segundos).
  expiry_time BIGINT NOT NULL,             -- Marca de tiempo UNIX para cuando expira la sesión.
  principal_name VARCHAR(100),             -- Nombre del usuario autenticado, si está disponible.
  CONSTRAINT spring_session_pk PRIMARY KEY (primary_id)  -- Clave primaria.
);

CREATE INDEX spring_session_ix1 ON spring_session (expiry_time);  -- Índice para sesiones expiradas.
CREATE INDEX spring_session_ix2 ON spring_session (session_id);   -- Índice para búsquedas por ID de sesión.
CREATE INDEX spring_session_ix3 ON spring_session (principal_name); -- Índice para búsquedas por usuario.


CREATE TABLE spring_session_attributes (
  session_primary_id VARCHAR(36) NOT NULL, -- ID único que relaciona el atributo con una sesión.
  attribute_name VARCHAR(200) NOT NULL,   -- Nombre del atributo.
  attribute_bytes BYTEA NOT NULL,         -- Valor del atributo almacenado como binario.
  CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name), -- Clave compuesta.
  CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id)
  REFERENCES spring_session (primary_id) ON DELETE CASCADE  -- Relación con la tabla `spring_session`.
);

CREATE TABLE historial_estados_pq (
  id serial PRIMARY KEY,
  pq_id integer NOT NULL,
  estado_id integer NOT NULL,
  usuario_id integer, -- opcional: para guardar quién cambió el estado
  observacion text,   -- opcional: observación asociada al cambio
  fecha_cambio timestamp DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE historial_estados_pq ADD FOREIGN KEY (pq_id) REFERENCES pqs(id);
ALTER TABLE historial_estados_pq ADD FOREIGN KEY (estado_id) REFERENCES estados_pq(id);
ALTER TABLE historial_estados_pq ADD FOREIGN KEY (usuario_id) REFERENCES usuarios(id);
