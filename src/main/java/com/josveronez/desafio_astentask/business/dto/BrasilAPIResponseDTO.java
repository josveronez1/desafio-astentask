package com.josveronez.desafio_astentask.business.dto;

import java.time.LocalDate;

public record BrasilAPIResponseDTO(
        LocalDate date,
        String name,
        String type
) {
}
