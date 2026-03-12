package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskUpdateDTO(
        @Size(min = 1, max = 255)
        String title,
        @Size(max = 2000)
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        Double estimatedHours

) {
}
