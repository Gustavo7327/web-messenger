package br.com.web.messenger.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(@NotBlank(message = "o email é obrigatório") @Email(message = "Email inválido") String email, @NotBlank(message = "O código é obrigatório") @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 dígitos") String code, @NotBlank(message = "A senha é obrigatória") @Size(min = 8, message = "A senha deve conter ao menos 8 caracteres") String newPassword) {
}
