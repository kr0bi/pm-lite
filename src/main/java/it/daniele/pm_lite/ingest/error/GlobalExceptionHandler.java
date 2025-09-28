package it.daniele.pm_lite.ingest.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ApiError.of("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> onConstraint(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(ApiError.of("CONSTRAINT_VIOLATION", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onAny(Exception ex) {
        return ResponseEntity.internalServerError().body(ApiError.of("INTERNAL_ERROR", ex.getMessage()));
    }
}
