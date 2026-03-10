package com.josveronez.desafio_astentask.business.mappers;

import com.josveronez.desafio_astentask.business.dto.CommentRequestDTO;
import com.josveronez.desafio_astentask.business.dto.CommentResponseDTO;
import com.josveronez.desafio_astentask.domain.entities.Comment;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.User;

public class CommentMapper {

    public static Comment toEntity(CommentRequestDTO request, Task task, User author) {
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setTask(task);
        comment.setAuthor(author);
        return comment;
    }

    public static CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getName() : "Autor desconhecido.",
                comment.getCreatedAt()
        );
    }
}
