package br.com.web.messenger.dto.auth;

public record LoginResponse(String token, Long expirationTime) {
    
}
