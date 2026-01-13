package br.com.web.messenger.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(@NotBlank(message = "O código é obrigatório") @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 dígitos") String code) {
}
