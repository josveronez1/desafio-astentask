package com.josveronez.desafio_astentask.business.dto;

import java.time.LocalDateTime;

public record TimeLogRequestDTO(
        Long taskId,
        Long userId,
        Double hoursWorked,
        String description,
        LocalDateTime logDate
) {
}
