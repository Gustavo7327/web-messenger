package br.com.web.messenger.dto.contact;

import jakarta.validation.constraints.NotBlank;

public record SaveContact(@NotBlank(message = "O nome de usuário é obrigatório") String username, @NotBlank(message = "O apelido é obrigatório") String nickname) {
}
