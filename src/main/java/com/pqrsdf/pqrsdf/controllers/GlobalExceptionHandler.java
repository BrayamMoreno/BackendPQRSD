package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.exceptions.DniAlreadyExistsException;
import com.pqrsdf.pqrsdf.exceptions.EmailAlreadyExistsException;
import com.pqrsdf.pqrsdf.utils.ResponseEntityUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Mensaje> handleRuntime(RuntimeException ex) {
        return ResponseEntityUtil.handleInternalError(ex);
    }

}
