package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.CommentRequestDTO;
import com.josveronez.desafio_astentask.business.dto.CommentResponseDTO;
import com.josveronez.desafio_astentask.business.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Tag(name = "Comentários", description = "Endpoints para comentários")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation(summary = "Listar comentários")
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> findByTask(@PathVariable Long taskId,
                                                               @ParameterObject @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(commentService.findByTaskId(taskId, pageable));
    }

    @Operation(summary = "Adicionar comentário")
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> create(@PathVariable Long taskId, @RequestBody CommentRequestDTO request) {
        CommentResponseDTO response = commentService.save(taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Editar comentário")
    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDTO> updateById(@PathVariable Long id, @RequestBody CommentRequestDTO request) {
        CommentResponseDTO response = commentService.updateById(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar comentário")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
