package org.example.company.exception;

import java.util.Objects;

public class EmailAlreadyExistsException extends BaseException {

    private final ErrorType errorType;
    private final String email;

    public EmailAlreadyExistsException(ErrorType errorType, String email) {
        super(EmailAlreadyExistsException.class, Objects.requireNonNull(errorType, "errorType must not be null").name());
        this.errorType = errorType;
        this.email = email;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        return "EmailAlreadyExistsException{" +
            "errorType=" + errorType +
            ", email='" + email + '\'' +
            '}';
    }
}
