package br.com.web.messenger.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@Email(message = "Email inválido") @NotBlank(message = "O email é obrigatório") String email, @NotBlank(message = "A senha é obrigatória") @Size(min = 8, message = "A senha deve ter ao menos 8 caracteres") String password) {
    
}
