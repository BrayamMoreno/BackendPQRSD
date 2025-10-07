package com.pqrsdf.pqrsdf.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pqrsdf.pqrsdf.Specifications.UsuarioSpecification;
import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.generic.GenericController;
import com.pqrsdf.pqrsdf.models.Usuario;
import com.pqrsdf.pqrsdf.service.UsuarioService;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/usuarios")
@Tag(name = "Gestion de Usuarios")
public class UsuarioController extends GenericController<Usuario, Long> {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        super(service);
        this.service = service;
    }

    @PatchMapping("/disable-account/{id}")
    public ResponseEntity<?> disableAccount(@PathVariable Long id) {
        try {

            if (id == null || id <= 0) {
                return ResponseEntityUtil.handleBadRequest("ID de usuario inválido.");
            }
            service.disableAccount(id);
            return ResponseEntity.ok("Cuenta deshabilitada correctamente.");
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PatchMapping("/enable-account/{id}")
    public ResponseEntity<?> enableAccount(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntityUtil.handleBadRequest("ID de usuario inválido.");
            }
            service.enabbleAccount(id);
            return ResponseEntity.ok("Cuenta habilitada correctamente.");
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsuarios(
            @RequestParam(required = false, defaultValue = "id") String order_by,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) Long rolId,
            @RequestParam(required = false) Boolean estado
            ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order_by));

            Specification<Usuario> spec = Specification
                    .where(UsuarioSpecification.hasNombreOrApellidoorCorreo(busqueda))
                    .and(UsuarioSpecification.hasRolId(rolId))
                    .and(UsuarioSpecification.hasEstado(estado));

                return ResponseEntityUtil.handlePaginationRequest(service.findAll(pageable, spec));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @Override
    @PostMapping
    @Operation(summary = "Crea una nueva entidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Guarda creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos no válidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto, datos duplicados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createEntity(@RequestBody Usuario entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createEntity(entity));
        } catch (DataIntegrityViolationException ex) {
            Throwable rootCause = ex.getRootCause();

            String message = rootCause != null ? rootCause.getMessage() : ex.getMessage();

            // Verificar si es una violación de clave foránea
            if (message.contains("foreign key") || message.contains("llave foránea")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Mensaje("Error: Clave foránea violada. Verifique las relaciones antes de insertar."));
            }

            // Verificar si es una violación de clave única
            if (message.contains("duplicate key") || message.contains("UNIQUE")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new Mensaje("Error: Ya existe un registro con estos datos."));
            }

            // Manejar otros errores de integridad
            return ResponseEntityUtil.handleBadRequest("Error de integridad de datos: " + message);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
