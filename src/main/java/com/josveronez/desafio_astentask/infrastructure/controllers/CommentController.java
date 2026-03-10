package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.CommentRequestDTO;
import com.josveronez.desafio_astentask.business.dto.CommentResponseDTO;
import com.josveronez.desafio_astentask.business.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> findByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.findByTaskId(taskId));
    }

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> create(@PathVariable Long taskId, @RequestBody CommentRequestDTO request) {
        CommentResponseDTO response = commentService.save(taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDTO> updateById(@PathVariable Long id, @RequestBody CommentRequestDTO request) {
        CommentResponseDTO response = commentService.updateById(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
