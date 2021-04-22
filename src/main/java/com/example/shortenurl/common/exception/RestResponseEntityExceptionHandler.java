package com.example.shortenurl.common.exception;

import com.example.shortenurl.dto.response.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    @ExceptionHandler(value = {
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            AuthenticationException.class
    })
    protected ResponseEntity handleRestException(Exception ex) {
        ResponseDTO.ResponseBuilder responseBuilder = ResponseDTO.ResponseBuilder.newBuilder();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (ex instanceof MethodArgumentNotValidException) {
            // Field errors
            Map<String, String> errors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid"));
            // Global errors
            errors.putAll(((MethodArgumentNotValidException) ex).getBindingResult().getGlobalErrors().stream().collect(Collectors.toMap(ObjectError::getObjectName, objectError -> objectError.getDefaultMessage() != null ? objectError.getDefaultMessage() : "Invalid")));
            logger.error("Validation failed MethodArgumentNotValidException: " + ex);
            responseBuilder.errors(errors).message("Validation failed");
        } else if (ex instanceof ConstraintViolationException) {
            Map<String, String> errors = (((ConstraintViolationException) ex).getConstraintViolations().stream().collect(Collectors.toMap((ConstraintViolation cv) -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage)));
            logger.error("Validation failed ConstraintViolationException: " + ex);
            responseBuilder.errors(errors).message("Validation failed");
        } else if (ex instanceof HttpMessageNotReadableException) {
            responseBuilder.message("Required request body is missing");
        } else {
            // This should not occur
            responseBuilder.message("There's no appropriate exception handler");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBuilder.build(), httpStatus);
    }
}
