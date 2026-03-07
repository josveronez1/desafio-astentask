package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.UserRole;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        UserRole role
) {
}
