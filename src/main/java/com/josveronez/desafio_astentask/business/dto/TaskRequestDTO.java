package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


public record TaskRequestDTO(

        @NotNull(message = "ID do projeto é obrigatório")
        Long projectId,
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255)
        String title,
        @Size(max = 2000)
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        @NotNull(message = "ID do relator é obrigatório")
        Long reporterId,
        LocalDateTime dueDate,
        Double estimatedHours

) {
}
