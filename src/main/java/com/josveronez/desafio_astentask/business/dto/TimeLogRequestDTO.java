package com.josveronez.desafio_astentask.business.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TimeLogRequestDTO(
        Long taskId,
        @NotNull(message = "ID do usuário é obrigatório")
        Long userId,
        @NotNull(message = "Horas trabalhadas é obrigatório")
        @Positive(message = "Horas trabalhadas deve ser positivo")
        Double hoursWorked,
        String description,
        @NotNull(message = "Data do registro é obrigatória")
        LocalDateTime logDate
) {
}
