package net.codejava.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.RequiredArgsConstructor;
import net.codejava.responses.Response;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<Response<String>> handlingAppException(AppException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<Response<String>> handlingRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, String> result = new HashMap<>();
        result.put("error", errors.getFirst());
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), result.get("error")));
    }

    @ExceptionHandler(value = ValidationException.class)
    ResponseEntity<Response<String>> handlingMethodArgumentNotValidException(ValidationException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<Response<String>> handlingConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<Response<String>> handlingMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), ex.getParameterName() + " is empty"));
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    ResponseEntity<Response<String>> handlingInvalidFormatException(InvalidFormatException ex) {
        return ResponseEntity.badRequest()
                .body(Response.failedResponse(HttpStatus.BAD_REQUEST.value(), "The string is not format"));
    }
}
