package org.example.company.exceptions;

import java.util.Objects;

public class BusinessRuleViolationException extends BaseException {

    private final ErrorType errorType;

    public BusinessRuleViolationException(ErrorType errorType) {
        super(BusinessRuleViolationException.class, Objects.requireNonNull(errorType, "errorType must not be null").name());
        this.errorType = errorType;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return String.format("BusinessRuleViolationException, errorType: %s", errorType.name());
    }
}
