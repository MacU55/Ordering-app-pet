package org.example.company.exceptions;

import lombok.Getter;

public abstract class BaseException extends RuntimeException {

    public BaseException(Class<? extends BaseException> clazz, String message) {
        super (String.format("%s : %s", clazz.getSimpleName(), message));
    }

    public abstract ErrorType getErrorType();


    @Getter
    public enum ErrorType {

        BUSINESS_RULE_VIOLATION("Business rule violation"),
        EMAIL_ALREADY_EXISTS("Email already exists"),
        INTERNAL_SERVER_ERROR("Internal Server Error"),
        ACCESS_DENIED("Access denied");


        private final String message;
        ErrorType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {return message;}

    }
}
