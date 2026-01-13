package br.com.web.messenger.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(@Email(message = "Email inválido") @NotBlank(message = "O email é obrigatório") String email) {
}
