package com.rezerve.authservice.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Database constraint violation";

        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause instanceof ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();

            if ("users_email_key".equals(constraintName)) {
                message = "Email must be unique";
            } else if ("users_phone_number_key".equals(constraintName)) {
                message = "Phone number must be unique";
            }
        } else if (rootCause.getMessage() != null) {
            // fallback for cases where Hibernate doesn't unwrap
            String causeMessage = rootCause.getMessage().toLowerCase();
            if (causeMessage.contains("users_email_key")) {
                message = "Email must be unique";
            } else if (causeMessage.contains("users_phone_number_key")) {
                message = "Phone number must be unique";
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                message,
                ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
