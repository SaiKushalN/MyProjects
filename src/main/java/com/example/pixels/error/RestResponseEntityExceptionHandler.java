package com.example.pixels.error;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorMessage> movieNotFoundException(ItemNotFoundException exception,
                                                               WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @ExceptionHandler(InvalidMovieException.class)
    public ResponseEntity<ErrorMessage> invalidMovieException(InvalidMovieException exception,
                                                               WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(SameDataUpdateExceptionHandler.class)
    public ResponseEntity<ErrorMessage> sameDataUpdateException(SameDataUpdateExceptionHandler exception,
                                                              WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(message);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorMessage> databaseDisconnectException(ConnectException exception,
                                                                    WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.SERVICE_UNAVAILABLE,
                exception.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Input error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleInternalServerException(HttpServerErrorException.InternalServerError exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Server Error: ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNoSuchElementFoundException(NoSuchElementException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Input Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, String>> handleForbiddenExceptionException(ForbiddenException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Forbidden: ",exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

}
