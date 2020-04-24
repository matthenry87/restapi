package com.matthenry87.restapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<Error> errors = fieldErrors.stream()
                .map(x -> new Error(x.getField(), x.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<Error>> constraintViolationException(ConstraintViolationException e) {

        List<Error> errors = new ArrayList<>();

        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {

            Path propertyPath = constraintViolation.getPropertyPath();

            String name = ((PathImpl) propertyPath).getLeafNode().getName();
            String message = constraintViolation.getMessage();

            errors.add(new Error(name, message));
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> alreadyExistsException(AlreadyExistsException e) {

        Error error = new Error(null, "already exists");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> notFoundException(NotFoundException e) {

        Error error = new Error(null, "not found");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> httpMessageNotReadableException(HttpMessageNotReadableException e) {

        Throwable cause = e.getCause();

        if(cause instanceof InvalidFormatException) {

            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;

            Class<?> targetType = invalidFormatException.getTargetType();

            if (Enum.class.isAssignableFrom(targetType)) {

                Enum[] enumConstants = (Enum[]) targetType.getEnumConstants();

                String values = Arrays.stream(enumConstants)
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

                String message  = "Invalid value. Valid values: " + values;
                String field = invalidFormatException.getPath().get(0).getFieldName();

                Error error = new Error(field, message);

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }

        Error error = new Error(null, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        Error error = new Error(null, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> exception(Exception e) {

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @AllArgsConstructor
    class Error {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final String field;
        private final String message;

    }

}
