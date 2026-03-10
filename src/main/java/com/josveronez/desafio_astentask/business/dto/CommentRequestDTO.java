package com.josveronez.desafio_astentask.business.dto;

public record CommentRequestDTO(
        String content,
        Long taskId,
        Long authorId
) {
}
