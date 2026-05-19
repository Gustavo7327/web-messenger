package br.com.web.messenger.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateGroup(@Size(max = 100, message = "O máximo de caracteres é 100") @NotBlank(message = "O nome não pode ser vazio") String name, @Size(max = 2000, message = "O máximo de caracteres é 2000") String description) {
}
