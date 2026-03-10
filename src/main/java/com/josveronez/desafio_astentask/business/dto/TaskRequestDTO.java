package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;

import java.time.LocalDateTime;


public record TaskRequestDTO(

        Long projectId,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        Long reporterId,
        LocalDateTime dueDate,
        Double estimatedHours

) {
}
