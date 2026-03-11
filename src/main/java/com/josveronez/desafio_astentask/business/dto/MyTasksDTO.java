package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;

import java.time.LocalDateTime;

public record MyTasksDTO(
        Long id,
        String title,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate
) {
}
