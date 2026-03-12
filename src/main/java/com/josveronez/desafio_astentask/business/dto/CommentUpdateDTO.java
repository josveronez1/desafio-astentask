package com.josveronez.desafio_astentask.business.dto;

import jakarta.validation.constraints.Size;

public record CommentUpdateDTO(
        @Size(min = 1, max = 5000)
        String content
) {
}
