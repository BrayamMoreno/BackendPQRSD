package com.pqrsdf.pqrsdf.controllers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pqrsdf.pqrsdf.dto.Mensaje;
import com.pqrsdf.pqrsdf.exceptions.EmailAlreadyExistsException;

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

    // Excepción de correo ya registrado
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Mensaje> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(new Mensaje(ex.getMessage()));
    }

    // Fallback genérico para RuntimeException (evita que explote con 500 sin
    // control)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Mensaje> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Mensaje("Error inesperado: " + ex.getMessage()));
    }

}
