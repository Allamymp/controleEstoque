package org.linx.apick.exception;

import jakarta.persistence.EntityNotFoundException;
import org.linx.apick.config.log.RedisLogger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final RedisLogger logger;

    public GlobalExceptionHandler(RedisLogger logger) {
        this.logger = logger;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.log("error", "Unhandled exception: " + ex.getMessage() + "\nStack Trace: " + getStackTrace(ex));
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Please try again later.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(EntityNotFoundException ex) {
        logger.log("warn", "Entity not found: " + ex.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicatedEmailException ex) {
        logger.log("warn", "Duplicated email: " + ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.log("warn", "Validation exception: " + ex.getMessage());
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value"));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.log("warn", "Illegal argument: " + ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid request. " + ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.log("error", "Data integrity violation: " + ex.getMessage());
        return buildResponseEntity(HttpStatus.CONFLICT, "Data conflict. Please check your input and try again.");
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleTransactionSystemException(TransactionSystemException ex) {
        logger.log("error", "Transaction system exception: " + ex.getMessage() + "\nStack Trace: " + getStackTrace(ex));
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction system error. Please try again later.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.log("error", "Runtime exception: " + ex.getMessage() + "\nStack Trace: " + getStackTrace(ex));
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(message));
    }

    private String getStackTrace(Throwable ex) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }

    // Classe para estruturar as respostas de erro
    public static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
