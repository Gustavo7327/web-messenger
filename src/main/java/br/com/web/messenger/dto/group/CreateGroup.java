package br.com.web.messenger.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGroup(@NotBlank(message = "Nome não pode estar vazio") @Size(max = 100, message = "O máximo de caracteres é 100") String name, @Size(max = 2000, message = "O máximo de caracteres é 2000") String description) {
}
