-- ==============================
-- TABLAS CON SOFT DELETE (deleted_at)
-- ==============================

CREATE TABLE "adjuntos_pq" (
  "id" SERIAL PRIMARY KEY,
  "pq_id" INTEGER NOT NULL,
  "nombre_archivo" VARCHAR NOT NULL,
  "ruta_archivo" VARCHAR NOT NULL,
  "created_at" TIMESTAMP,
  "respuesta" BOOLEAN,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "areas_resp" (
  "id" SERIAL PRIMARY KEY,
  "codigo_dep" VARCHAR NOT NULL,
  "nombre" VARCHAR NOT NULL,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "personas" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL,
  "apellido" VARCHAR NOT NULL,
  "tipo_doc" INTEGER,
  "dni" VARCHAR NOT NULL,
  "fecha_nacimiento" DATE,
  "tipo_persona" INTEGER,
  "telefono" VARCHAR NOT NULL,
  "direccion" VARCHAR,
  "tratamiento_datos" BOOLEAN NOT NULL,
  "municipio_id" INTEGER,
  "genero" INTEGER NOT NULL,
  "created_at" TIMESTAMP,
  "updated_at" TIMESTAMP,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "pqs" (
  "id" SERIAL PRIMARY KEY,
  "numero_radicado" VARCHAR,
  "detalle_asunto" TEXT NOT NULL,
  "tipo_pq_id" INTEGER,
  "solicitante_id" INTEGER,
  "fecha_radicacion" DATE,
  "hora_radicacion" TIME,
  "fecha_resolucion_estimada" DATE,
  "fecha_resolucion" DATE,
  "radicador_id" INTEGER,
  "responsable_id" INTEGER,
  "web" BOOLEAN NOT NULL,
  "respuesta" TEXT,
  "detalle_descripcion" VARCHAR,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "historial_estados_pq" (
  "id" SERIAL PRIMARY KEY,
  "pq_id" INTEGER NOT NULL,
  "estado_id" INTEGER NOT NULL,
  "cambiado_por_id" INTEGER,
  "observacion" TEXT,
  "fecha_cambio" TIMESTAMP,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "responsables_pq" (
  "id" SERIAL PRIMARY KEY,
  "persona_responsable_id" INTEGER,
  "area_id" INTEGER,
  "fecha_asignacion" TIMESTAMP,
  "is_active" BOOLEAN,
  "deleted_at" TIMESTAMP NULL
);

CREATE TABLE "usuarios" (
  "id" SERIAL PRIMARY KEY,
  "correo" VARCHAR NOT NULL,
  "contrasena" VARCHAR NOT NULL,
  "is_enable" BOOLEAN,
  "reset_token" VARCHAR,
  "reset_token_expiration" TIMESTAMP,
  "persona_id" INTEGER,
  "rol_id" INTEGER,
  "created_at" TIMESTAMP,
  "updated_at" TIMESTAMP,
  "deleted_at" TIMESTAMP NULL
);

-- ==============================
-- TABLAS SIN SOFT DELETE (base del sistema)
-- ==============================

CREATE TABLE "audit_log" (
  "id" SERIAL PRIMARY KEY,
  "username" VARCHAR,
  "action" VARCHAR,
  "method" VARCHAR,
  "endpoint" TEXT,
  "status_code" INTEGER,
  "timestamp" TIMESTAMP
);

CREATE TABLE "departamentos" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR,
  "codigo_dane" VARCHAR
);

CREATE TABLE "estados_pq" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL,
  "color" VARCHAR,
  "descripcion" VARCHAR
);

CREATE TABLE "generos" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL
);

CREATE TABLE "municipios" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR,
  "codigo_dane" VARCHAR,
  "departamento_id" INTEGER NOT NULL
);

CREATE TABLE "permisos" (
  "id" SERIAL PRIMARY KEY,
  "tabla" VARCHAR NOT NULL,
  "accion" VARCHAR NOT NULL,
  "descripcion" TEXT
);

CREATE TABLE "roles" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL,
  "created_at" DATE DEFAULT CURRENT_DATE,
  "updated_at" DATE,
  "descripcion" VARCHAR
);

CREATE TABLE "roles_permisos" (
  "id" SERIAL PRIMARY KEY,
  "rol_id" INTEGER,
  "permiso_id" INTEGER
);

CREATE TABLE "tipos_docs" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL
);

CREATE TABLE "tipos_personas" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL
);

CREATE TABLE "tipos_pq" (
  "id" SERIAL PRIMARY KEY,
  "nombre" VARCHAR NOT NULL,
  "dias_habiles_respuesta" INTEGER NOT NULL
);

-- ==============================
-- RELACIONES FK
-- ==============================

ALTER TABLE "adjuntos_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");
ALTER TABLE "municipios" ADD FOREIGN KEY ("departamento_id") REFERENCES "departamentos" ("id");
ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_doc") REFERENCES "tipos_docs" ("id");
ALTER TABLE "personas" ADD FOREIGN KEY ("tipo_persona") REFERENCES "tipos_personas" ("id");
ALTER TABLE "personas" ADD FOREIGN KEY ("municipio_id") REFERENCES "municipios" ("id");
ALTER TABLE "personas" ADD FOREIGN KEY ("genero") REFERENCES "generos" ("id");
ALTER TABLE "pqs" ADD FOREIGN KEY ("tipo_pq_id") REFERENCES "tipos_pq" ("id");
ALTER TABLE "pqs" ADD FOREIGN KEY ("solicitante_id") REFERENCES "personas" ("id");
ALTER TABLE "pqs" ADD FOREIGN KEY ("radicador_id") REFERENCES "usuarios" ("id");
ALTER TABLE "pqs" ADD FOREIGN KEY ("responsable_id") REFERENCES "usuarios" ("id");
ALTER TABLE "historial_estados_pq" ADD FOREIGN KEY ("pq_id") REFERENCES "pqs" ("id");
ALTER TABLE "historial_estados_pq" ADD FOREIGN KEY ("estado_id") REFERENCES "estados_pq" ("id");
ALTER TABLE "historial_estados_pq" ADD FOREIGN KEY ("cambiado_por_id") REFERENCES "usuarios" ("id");
ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");
ALTER TABLE "roles_permisos" ADD FOREIGN KEY ("permiso_id") REFERENCES "permisos" ("id");
ALTER TABLE "usuarios" ADD FOREIGN KEY ("persona_id") REFERENCES "personas" ("id");
ALTER TABLE "usuarios" ADD FOREIGN KEY ("rol_id") REFERENCES "roles" ("id");
ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("persona_responsable_id") REFERENCES "personas" ("id");
ALTER TABLE "responsables_pq" ADD FOREIGN KEY ("area_id") REFERENCES "areas_resp" ("id");

-- ==============================
-- VISTAS (sin cambios)
-- ==============================

CREATE OR REPLACE VIEW vista_resumen_radicador AS
SELECT
    p.radicador_id,
    COUNT(*) FILTER (WHERE he.estado_id IN (1, 2, 3, 4)) AS radicadas,   -- Estados 1 a 4
    COUNT(*) FILTER (WHERE he.estado_id = 2) AS asignadas,               -- Estado 2
    COUNT(*) FILTER (WHERE he.estado_id = 3) AS rechazadas,              -- Estado 4
    COUNT(*) FILTER (WHERE he.estado_id = 1) AS por_asignar              -- Estado 1
FROM pqs p
LEFT JOIN historial_estados_pq he
    ON he.pq_id = p.id
WHERE
    p.deleted_at IS NULL
    AND he.deleted_at IS NULL
    AND he.fecha_cambio = (
    SELECT MAX(he2.fecha_cambio)
    FROM historial_estados_pq he2
    WHERE he2.pq_id = p.id
            AND he2.deleted_at IS NULL
)
GROUP BY p.radicador_id;

CREATE OR REPLACE VIEW vista_conteo_pq AS
SELECT
    p.solicitante_id,
    COUNT(*) FILTER (WHERE ult.estado_id = 4) AS resueltas,   -- Cerrada
    COUNT(*) FILTER (WHERE ult.estado_id = 3) AS rechazadas,  -- Rechazada
    COUNT(*) FILTER (WHERE ult.estado_id IN (1, 2)) AS pendientes  -- Radicada o Asignada
FROM pqs p
JOIN LATERAL (
    SELECT he.estado_id
    FROM historial_estados_pq he
    WHERE he.pq_id = p.id
      AND he.deleted_at IS NULL       -- Ignorar historiales eliminados (soft delete)
    ORDER BY he.fecha_cambio DESC
    LIMIT 1
) ult ON true
WHERE
    p.solicitante_id IS NOT NULL
    AND p.deleted_at IS NULL          -- Ignorar PQs eliminadas
GROUP BY p.solicitante_id;


INSERT INTO estados_pq (id, nombre, color, descripcion) VALUES
(1, 'Radicada', '#f3991b', 'Estado que sirve para denotar que una petición ya fue registrada en el sistema'),
(2, 'Asignada', '#3B82F6', 'Estado que indica que la petición ha sido asignada a un responsable para su gestión'),
(3, 'Rechazada', '#EF4444', 'Estado que señala que la petición fue revisada y no aprobada o no procede'),
(4, 'Cerrada',   '#10B981', 'Estado que indica que la petición ha sido atendida y finalizada exitosamente');
-- Asegura que la secuencia del SERIAL continúe después del último id insertado
ALTER SEQUENCE estados_pq_id_seq RESTART WITH 5;

INSERT INTO roles (id, nombre, created_at, updated_at, descripcion) VALUES
(1, 'Admin', NULL, '2025-10-26', 'Rol diseñado para el administrador del sistema'),
(2, 'Funcionario STTG', NULL, '2025-10-20', 'Rol para las personas que realizan la resolución de las solicitudes'),
(3, 'Asignador STTG', NULL, '2025-10-26', 'Rol diseñado para el acceso al dashboard de radicación'),
(4, 'Usuario', NULL, '2025-10-23', 'Rol definido para los usuarios del aplicativo web');
-- Ajustar la secuencia para continuar después del último ID insertado
ALTER SEQUENCE roles_id_seq RESTART WITH 6;

INSERT INTO tipos_docs (nombre) VALUES
('Cédula de Ciudadanía'),
('Tarjeta de Identidad'),
('Registro Civil de Nacimiento'),
('Cédula de Extranjería'),
('Pasaporte'),
('Número de Identificación Tributaria (NIT)'),
('Permiso Especial de Permanencia (PEP)'),
('Permiso por Protección Temporal (PPT)'),
('Documento Nacional de Identidad (DNI) extranjero'),
('Salvoconducto de Permanencia');

INSERT INTO tipos_personas (nombre) VALUES
('Natural'),
('Jurídica');

INSERT INTO generos (nombre) VALUES
('Hombre'),
('Mujer');


INSERT INTO permisos (id, tabla, accion, descripcion) VALUES
(1, 'usuarios', 'agregar', 'Permite agregar usuarios'),
(2, 'usuarios', 'modificar', 'Permite modificar usuarios'),
(3, 'usuarios', 'eliminar', 'Permite eliminar usuarios'),
(4, 'usuarios', 'leer', 'Permite ver usuarios'),
(5, 'roles', 'agregar', 'Permite agregar roles'),
(6, 'roles', 'modificar', 'Permite modificar roles'),
(7, 'roles', 'eliminar', 'Permite eliminar roles'),
(8, 'roles', 'leer', 'Permite ver roles'),
(9, 'areas_responsables', 'agregar', 'Permite agregar áreas responsables'),
(10, 'areas_responsables', 'modificar', 'Permite modificar áreas responsables'),
(11, 'areas_responsables', 'eliminar', 'Permite eliminar áreas responsables'),
(12, 'areas_responsables', 'leer', 'Permite ver áreas responsables'),
(13, 'departamentos', 'agregar', 'Permite agregar departamentos'),
(14, 'departamentos', 'modificar', 'Permite modificar departamentos'),
(15, 'departamentos', 'eliminar', 'Permite eliminar departamentos'),
(16, 'departamentos', 'leer', 'Permite ver departamentos'),
(17, 'pqs', 'agregar', 'Permite agregar PQRs'),
(18, 'pqs', 'modificar', 'Permite modificar PQRs'),
(19, 'pqs', 'eliminar', 'Permite eliminar PQRs'),
(20, 'pqs', 'leer', 'Permite ver PQRs'),
(21, 'historial_estados', 'agregar', 'Permite agregar historial de estados de PQRs'),
(22, 'historial_estados', 'modificar', 'Permite modificar historial de estados de PQRs'),
(23, 'historial_estados', 'eliminar', 'Permite eliminar historial de estados de PQRs'),
(24, 'historial_estados', 'leer', 'Permite ver historial de estados de PQRs'),
(25, 'usuario', 'dashboard', 'Permite acceder al dashboard de gestión de PQRs'),
(26, 'funcionario', 'dashboard', 'Permite acceder al dashboard de estadísticas'),
(27, 'admin', 'dashboard', 'Permite acceder al dashboard de administración'),
(30, 'asignador', 'dashboard', 'Permite acceder al dashboard de radicacion'),
(32, 'adjuntos_pq', 'agregar', 'Permite agregar adjuntos de PQRs'),
(33, 'adjuntos_pq', 'modificar', 'Permite modificar adjuntos de PQRs'),
(34, 'adjuntos_pq', 'eliminar', 'Permite eliminar adjuntos de PQRs'),
(35, 'adjuntos_pq', 'leer', 'Permite ver adjuntos de PQRs'),
(36, 'estados_pqs', 'agregar', 'Permite agregar estados de PQRs'),
(37, 'estados_pqs', 'modificar', 'Permite modificar estados de PQRs'),
(38, 'estados_pqs', 'eliminar', 'Permite eliminar estados de PQRs'),
(39, 'estados_pqs', 'leer', 'Permite ver estados de PQRs'),
(40, 'generos', 'agregar', 'Permite agregar géneros'),
(41, 'generos', 'modificar', 'Permite modificar géneros'),
(42, 'generos', 'eliminar', 'Permite eliminar géneros'),
(43, 'generos', 'leer', 'Permite ver géneros'),
(44, 'municipios', 'agregar', 'Permite agregar municipios'),
(45, 'municipios', 'modificar', 'Permite modificar municipios'),
(46, 'municipios', 'eliminar', 'Permite eliminar municipios'),
(47, 'municipios', 'leer', 'Permite ver municipios'),
(48, 'personas', 'agregar', 'Permite agregar personas'),
(49, 'personas', 'modificar', 'Permite modificar personas'),
(50, 'personas', 'eliminar', 'Permite eliminar personas'),
(51, 'personas', 'leer', 'Permite ver personas'),
(52, 'responsables_pqs', 'agregar', 'Permite agregar responsables de PQRs'),
(53, 'responsables_pqs', 'modificar', 'Permite modificar responsables de PQRs'),
(54, 'responsables_pqs', 'eliminar', 'Permite eliminar responsables de PQRs'),
(55, 'responsables_pqs', 'leer', 'Permite ver responsables de PQRs'),
(56, 'roles_permisos', 'agregar', 'Permite asignar permisos a roles'),
(57, 'roles_permisos', 'modificar', 'Permite modificar permisos de roles'),
(58, 'roles_permisos', 'eliminar', 'Permite eliminar permisos de roles'),
(59, 'roles_permisos', 'leer', 'Permite ver permisos de roles'),
(60, 'tipos_documentos', 'agregar', 'Permite agregar tipos de documentos'),
(61, 'tipos_documentos', 'modificar', 'Permite modificar tipos de documentos'),
(62, 'tipos_documentos', 'eliminar', 'Permite eliminar tipos de documentos'),
(63, 'tipos_documentos', 'leer', 'Permite ver tipos de documentos'),
(64, 'tipos_personas', 'agregar', 'Permite agregar tipos de personas'),
(65, 'tipos_personas', 'modificar', 'Permite modificar tipos de personas'),
(66, 'tipos_personas', 'eliminar', 'Permite eliminar tipos de personas'),
(67, 'tipos_personas', 'leer', 'Permite ver tipos de personas'),
(68, 'tipos_pqs', 'agregar', 'Permite agregar tipos de PQRs'),
(69, 'tipos_pqs', 'modificar', 'Permite modificar tipos de PQRs'),
(70, 'tipos_pqs', 'eliminar', 'Permite eliminar tipos de PQRs'),
(71, 'tipos_pqs', 'leer', 'Permite ver tipos de PQRs'),
(72, 'permisos', 'leer', 'Permite ver permisos'),
(73, 'utilidades', 'acceder', 'Permite acceder al módulo de utilidades'),
(74, 'backups', 'agregar', 'Permite crear backups del sistema'),
(75, 'backups', 'eliminar', 'Permite eliminar backups del sistema'),
(76, 'backups', 'descargar', 'Permite descargar archivos de backup'),
(79, 'backups', 'leer', 'Permite leer backups del sistema'),
(80, 'logs', 'leer', 'Permite leer los logs de la aplicacion'),
(81, 'adjuntos_pq', 'descargar', 'Permite descargar los archivos de las peticiones'),
(78, 'reportes', 'generar', 'Permite generar reportes y estadísticas');
ALTER SEQUENCE permisos_id_seq RESTART WITH 82;

select *
from roles;

INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
(1, 8),
(1, 24),
(1, 58),
(1, 9),
(1, 78),
(1, 13),
(1, 59),
(1, 53),
(1, 17),
(1, 7),
(1, 60),
(1, 52),
(1, 23),
(1, 73),
(1, 57),
(1, 70),
(1, 54),
(1, 80),
(1, 38),
(1, 61),
(1, 65),
(1, 16),
(1, 35),
(1, 11),
(1, 15),
(1, 21),
(1, 44),
(1, 55),
(1, 41),
(1, 12),
(1, 64),
(1, 32),
(1, 46),
(1, 66),
(1, 10),
(1, 1),
(1, 48),
(1, 6),
(1, 81),
(1, 79),
(1, 4),
(1, 33),
(1, 19),
(1, 67),
(1, 75),
(1, 69),
(1, 43),
(1, 56),
(1, 27),
(1, 45),
(1, 68),
(1, 51),
(1, 34),
(1, 3),
(1, 20),
(1, 76),
(1, 63),
(1, 62),
(1, 42),
(1, 72),
(1, 39),
(1, 5),
(1, 47),
(1, 40),
(1, 22),
(1, 74),
(1, 18),
(1, 2),
(1, 14),
(1, 36),
(1, 37),
(1, 49),
(1, 71),
(1, 50),
(2, 47),
(2, 43),
(2, 32),
(2, 16),
(2, 67),
(2, 26),
(2, 49),
(2, 18),
(2, 63),
(2, 39),
(2, 20),
(2, 71),
(3, 71),
(3, 39),
(3, 81),
(3, 49),
(3, 30),
(3, 17),
(3, 12),
(3, 18),
(3, 55),
(3, 20),
(4, 81),
(4, 16),
(4, 43),
(4, 17),
(4, 49),
(4, 71),
(4, 47),
(4, 63),
(4, 67),
(4, 20),
(4, 39),
(4, 35),
(4, 25);

