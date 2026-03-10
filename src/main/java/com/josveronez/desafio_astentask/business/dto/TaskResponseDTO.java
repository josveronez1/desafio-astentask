package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String projectName,
        String assigneeName,
        String reporterName,
        Double estimatedHours,
        Double actualHours,
        LocalDateTime dueDate,
        LocalDateTime createdAt
) {

}
