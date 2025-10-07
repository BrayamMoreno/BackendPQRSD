package com.pqrsdf.pqrsdf.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pqrsdf.pqrsdf.dto.Mensaje;

public class ResponseEntityUtil {
    public static ResponseEntity<Mensaje> handleNotFoundError(String mensaje) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Mensaje(mensaje));
    }

    public static ResponseEntity<Mensaje> handleInternalError(Exception e) {
        String errorDetail = "Error interno del servidor: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Mensaje(errorDetail));
    }

    public static ResponseEntity<Mensaje> handleDuplicatedDataError(String mensaje) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Mensaje(mensaje));
    }

    public static ResponseEntity<Void> handleNoContent(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    public static ResponseEntity<Mensaje> handleBadRequest(String mensaje){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Mensaje(mensaje));
    }

    public static <T> ResponseEntity<Map<String, Object>> handlePaginationRequest(Page<T> entities) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", entities.getContent());
        response.put("total_count", entities.getTotalElements());
        response.put("has_more", entities.hasNext());
        response.put("page", entities.getNumber());
        response.put("items_per_page", entities.getSize());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
