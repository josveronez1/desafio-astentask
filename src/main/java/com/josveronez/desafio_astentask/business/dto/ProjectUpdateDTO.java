package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;
import jakarta.validation.constraints.Size;

public record ProjectUpdateDTO(
        @Size(min = 1, max = 255)
        String name,
        @Size(max = 2000)
        String description,
        ProjectStatus status,
        Long ownerId
) {
}
