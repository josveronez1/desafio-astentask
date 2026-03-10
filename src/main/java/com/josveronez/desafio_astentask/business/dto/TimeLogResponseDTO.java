package com.josveronez.desafio_astentask.business.dto;

import java.time.LocalDateTime;

public record TimeLogResponseDTO(
        Long id,
        Long taskId,
        Long userId,
        String userName,
        Double hoursWorked,
        String description,
        LocalDateTime logDate

) {
}
