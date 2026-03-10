package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.TaskRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TaskResponseDTO;
import com.josveronez.desafio_astentask.business.services.TaskService;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> findByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.findAllByProjectId(projectId));
    }

    @PostMapping("/api/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponseDTO> create(@PathVariable Long projectId, @RequestBody TaskRequestDTO request) {
        TaskResponseDTO response = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateById(@PathVariable Long id, @RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignUser(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.assignUser(id, userId));
    }

}
