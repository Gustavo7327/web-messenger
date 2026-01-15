package br.com.web.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String resourceName, String field, String value) {
        super(
            String.format("%s com %s '%s' já existe", resourceName, field, value),
            HttpStatus.CONFLICT
        );
    }
}
