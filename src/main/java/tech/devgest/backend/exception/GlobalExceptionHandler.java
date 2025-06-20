package tech.devgest.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                400,
                "Invalid request body",
                request.getRequestURI(),
                ex.getFieldErrors().stream().map(
                        fieldError -> new ErrorFieldDetail(fieldError.getField(), fieldError.getDefaultMessage())
                ).toList()
        );
        return ResponseEntity.status(400).body(err);
    }


    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                400,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(400).body(err);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadCredsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                401,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(400).body(err);
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request
    ){
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(404).body(err);
    }




}
