package com.josveronez.desafio_astentask.infrastructure.exceptions;

import com.josveronez.desafio_astentask.business.dto.ErrorResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ConflictException;
import com.josveronez.desafio_astentask.business.exceptions.ExternalAPIException;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex,
                                                           HttpServletRequest request) {
        log.warn("event=exception_handled exception=ResourceNotFoundException path={} message={}",
                request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(ConflictException ex,
                                                           HttpServletRequest request) {
        log.warn("event=exception_handled exception=ConflictException path={} message={}",
                request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ExternalAPIException.class)
    public ResponseEntity<ErrorResponseDTO> handleExternalApi(ExternalAPIException ex,
                                                              HttpServletRequest request) {
        log.error("event=exception_handled exception=ExternalAPIException path={} message={}",
                request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        List<ErrorResponseDTO.FieldErrorDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorResponseDTO.FieldErrorDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        log.warn("event=exception_handled exception=MethodArgumentNotValidException path={} fieldErrors={}",
                request.getRequestURI(), errors.size());
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex,
                                                          HttpServletRequest request) {
        log.error("event=exception_handled exception=Exception path={} message={}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor",
                request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status,
                                                           String message,
                                                           String path,
                                                           List<ErrorResponseDTO.FieldErrorDTO> fieldErrors) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}