package br.com.web.messenger.dto.contact;

import jakarta.validation.constraints.NotBlank;

public record UpdateContact(@NotBlank(message = "O apelido é obrigatório") String nickname) {
}
