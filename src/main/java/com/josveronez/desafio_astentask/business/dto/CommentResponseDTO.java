package com.josveronez.desafio_astentask.business.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String content,
        Long taskId,
        String authorName,
        LocalDateTime createdAt
) {
}
