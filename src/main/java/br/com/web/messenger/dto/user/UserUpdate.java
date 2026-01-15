package br.com.web.messenger.dto.user;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdate(
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String name,
    
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String password,
    
    @Size(max = 500, message = "Bio não pode ter mais de 500 caracteres")
    String bio,
    
    List<String> links
) {
    
}
