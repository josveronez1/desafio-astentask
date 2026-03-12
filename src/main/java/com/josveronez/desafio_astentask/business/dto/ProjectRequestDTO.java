package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequestDTO(
        @NotBlank(message = "Nome do projeto é obrigatório")
        @Size(max = 255)
        String name,
        @Size(max = 2000)
        String description,
        @NotNull(message = "ID do dono é obrigatório")
        Long ownerId,
        ProjectStatus status

) {
}
