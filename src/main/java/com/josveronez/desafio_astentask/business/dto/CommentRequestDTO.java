package com.josveronez.desafio_astentask.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequestDTO(
        @NotBlank(message = "Conteúdo é obrigatório")
        @Size(max = 5000)
        String content,
        Long taskId,
        @NotNull(message = "ID do autor é obrigatório")
        Long authorId
) {
}