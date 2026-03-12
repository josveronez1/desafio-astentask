package com.josveronez.desafio_astentask.business.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TimeLogRequestDTO(
        Long taskId,
        Long userId,
        @Positive
        Double hoursWorked,
        String description,
        LocalDateTime logDate
) {
}
