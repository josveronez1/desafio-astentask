package com.josveronez.desafio_astentask.business.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TimeLogUpdateDTO(
        @Positive(message = "Horas trabalhadas deve ser positivo")
        Double hoursWorked,
        String description,
        LocalDateTime logDate
) {
}
