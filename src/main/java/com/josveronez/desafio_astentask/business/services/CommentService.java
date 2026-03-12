package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.CommentRequestDTO;
import com.josveronez.desafio_astentask.business.dto.CommentResponseDTO;
import com.josveronez.desafio_astentask.business.dto.CommentUpdateDTO;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.CommentMapper;
import com.josveronez.desafio_astentask.domain.entities.Comment;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.repositories.CommentRepository;
import com.josveronez.desafio_astentask.domain.repositories.TaskRepository;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(TaskRepository taskRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDTO save(Long taskId, CommentRequestDTO request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada."));

        User author = userRepository.findById(request.authorId())
               .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Comment comment = CommentMapper.toEntity(request, task, author);

        return CommentMapper.toResponseDTO(commentRepository.save(comment));

    }

    public Page<CommentResponseDTO> findByTaskId(Long taskId, Pageable pageable) {
        if(!taskRepository.existsById(taskId)){
            throw new ResourceNotFoundException("Tarefa não encontrada.");
        }
        return commentRepository.findByTaskId(taskId, pageable)
                .map(CommentMapper::toResponseDTO);
    }

    public CommentResponseDTO updateById(Long id, CommentUpdateDTO request){
        Comment commentToUpdate = commentRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Comentário não encontrado.")
                );
        if (request.content() != null){
            commentToUpdate.setContent(request.content());
        }
        return CommentMapper.toResponseDTO(commentRepository.save(commentToUpdate));
    }

    public void deleteById(Long id){
        if (!commentRepository.existsById(id)){
            throw new ResourceNotFoundException("Comentário com id" + id + "não encontrado.");
        }
        commentRepository.deleteById(id);
    }

}
