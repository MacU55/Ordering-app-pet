package org.example.company.controller.errorHandling;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.company.exceptions.BusinessRuleViolationException;
import org.example.company.exceptions.EmailAlreadyExistsException;
import org.example.company.exceptions.ImpropriateRoleException;
import org.example.company.exceptions.InternalServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.example.company.dto.response.error.ErrorResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(" Resource is not found, {}", ex.getMessage());
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        var message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .toList();

        return new ErrorResponse("VALIDATION_ERROR", message.toString());
    }

    @ExceptionHandler(InternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServiceException(InternalServiceException ex) {
        return new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessRuleViolationException(BusinessRuleViolationException ex) {
        return new ErrorResponse("BUSINESS_RULE_VIOLATION", ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ErrorResponse("EMAIL_ALREADY_EXISTS", ex.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: {}", ex.getMessage());
        return new ErrorResponse("CONSTRAINT_VIOLATION", "Data integrity violation - possibly duplicate entry");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        return new ErrorResponse("INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(ImpropriateRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(ImpropriateRoleException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return new ErrorResponse("ACCESS_DENIED", ex.getMessage());
    }

}
