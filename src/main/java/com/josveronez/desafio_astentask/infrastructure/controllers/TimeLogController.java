package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.TimeLogRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TimeLogResponseDTO;
import com.josveronez.desafio_astentask.business.services.TimeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Time Logs", description = "Endpoints para gerenciamento de registros de tempo")
public class TimeLogController {

    private final TimeLogService timeLogService;
    public TimeLogController(TimeLogService timeLogService){
        this.timeLogService = timeLogService;
    }


    @Operation(summary = "Listar registros de tempo")
    @GetMapping("/tasks/{taskId}/timelogs")
    public ResponseEntity<List<TimeLogResponseDTO>> findByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(timeLogService.findByTaskId(taskId));
    }

    @Operation(summary = "Registrar tempo")
    @PostMapping("/tasks/{taskId}/timelogs")
    public ResponseEntity<TimeLogResponseDTO> create(@PathVariable Long taskId, @RequestBody TimeLogRequestDTO request) {
        TimeLogResponseDTO response = timeLogService.save(taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualizar registro")
    @PutMapping("/timelogs/{id}")
    public ResponseEntity<TimeLogResponseDTO> updatedById(@PathVariable Long id, @RequestBody TimeLogRequestDTO request) {
        TimeLogResponseDTO response = timeLogService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar registro")
    @DeleteMapping("/timelogs/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        timeLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
