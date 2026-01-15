package br.com.web.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
