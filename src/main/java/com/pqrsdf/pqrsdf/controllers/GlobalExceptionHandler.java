package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pqrsdf.pqrsdf.dto.Mensaje;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Mensaje> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensajeError = ex.getBindingResult().getFieldError() != null
                ? "Error en el campo " + ex.getBindingResult().getFieldError().getField() + ": "
                        + ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Error de validación desconocido";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(mensajeError));
    }

}
