package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;

public record ProjectRequestDTO(
        String name,
        String description,
        Long ownerId,
        ProjectStatus status

) {
}
