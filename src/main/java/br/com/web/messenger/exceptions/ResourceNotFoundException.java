package br.com.web.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(
            String.format("%s com identificador '%s' não encontrado", resourceName, identifier),
            HttpStatus.NOT_FOUND
        );
    }
}
