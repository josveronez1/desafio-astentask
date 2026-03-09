package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        Long ownerId
) {
}
