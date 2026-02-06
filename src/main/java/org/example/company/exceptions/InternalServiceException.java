package org.example.company.exceptions;

public class InternalServiceException extends BaseException {
    public InternalServiceException(ErrorType errorType) {
        super(InternalServiceException.class, errorType.name());
    }

    public ErrorType getErrorType() {
        return ErrorType.INTERNAL_SERVER_ERROR;
    }
}
