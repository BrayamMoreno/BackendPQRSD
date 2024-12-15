package com.pqrsdf.pqrsdf.generic;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class GenericController<T extends GenericEntity, ID> {
    
    private final GenericService<T, ID> service;
    private static final String ENTIDAD_NO_ENCONTRADA = "Entidad No Encontrada";
    private static final String RELACION_NO_ENCONTRADA = "Una o Unas de las Relaciones No Existe";

    public GenericController(GenericService<T, ID> service){
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las entidades paginadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getAllEntities(
        @RequestParam(required = false, defaultValue = "id") String order_by,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size)
    {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order_by));
            Page<T> entities = service.getAllEntitires(pageable);
            return ResponseEntityUtil.handlePaginationRequest(entities);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

      @GetMapping("/{id}")
    @Operation(summary = "Obtiene una entidad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad encontrado"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getEntityById(@PathVariable ID id) {
        try {
            T entity = service.getById(id);
            if(entity == null){
                return ResponseEntityUtil.handleNotFoundError(ENTIDAD_NO_ENCONTRADA);
            }
            return ResponseEntity.status(HttpStatus.OK).body(entity);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PostMapping
    @Operation(summary = "Crea una nueva entidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Guarda creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos no válidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto, datos duplicados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createEntity(@RequestBody T entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createEntity(entity));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntityUtil.handleBadRequest(RELACION_NO_ENCONTRADA);
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una entidad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos no válidos"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> partialUpdateEntity(
        @PathVariable ID id,
        @RequestBody T t){
        try {
            T entity = service.getById(id);
            if(entity == null){
                return ResponseEntityUtil.handleNotFoundError(ENTIDAD_NO_ENCONTRADA);
            }
            return ResponseEntity.status(HttpStatus.OK).body(service.updateEntity(entity, t));
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una entidad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteEntity(@PathVariable ID id) {
        try {
            if(!service.isPresent(id)){
                return ResponseEntityUtil.handleNotFoundError(ENTIDAD_NO_ENCONTRADA);
            }
            service.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntityUtil.handleInternalError(e);
        }
    }
}
