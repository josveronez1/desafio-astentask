package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 1, max = 255, message = "Nome não pode ser vazio")
        String name,
        @Email(message = "Email inválido")
        String email,
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,
        UserRole role
) {
}
