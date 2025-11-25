package com.eodi.bium.review.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        log.error("Business exception occurred: {}", e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
            .body(new CustomErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleUnknownException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CustomErrorResponse(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage()));
    }
}