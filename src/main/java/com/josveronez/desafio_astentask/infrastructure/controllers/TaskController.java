package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.TaskRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TaskResponseDTO;
import com.josveronez.desafio_astentask.business.services.TaskService;
import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @Operation(summary = "Lista tarefas de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de tarefas recuperada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponseDTO>> findByProject(
            @PathVariable Long projectId,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskService.findAllByProjectId(projectId, pageable));
    }

    @Operation(summary = "Buscar tarefas com filtros avançados",
            description = "Filtra por status, prioridade, assignee, projeto e intervalo de dueDate.")
    @ApiResponse(responseCode = "200", description = "Página de tarefas filtradas.")
    @GetMapping("/tasks/search")
    public ResponseEntity<Page<TaskResponseDTO>> searchTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dueDateFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dueDateTo,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                taskService.searchTasks(status, priority, assigneeId, projectId, dueDateFrom, dueDateTo, pageable)
        );
    }

    @Operation(summary = "Criar tarefa"
    , description = "Cria uma nova tarefa para um projeto + Verifica se a data não cai em nenhum feriado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou data em feriado"),
            @ApiResponse(responseCode = "404", description = "Projeto ou usuário não encontrado")})
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponseDTO> create(@PathVariable Long projectId, @RequestBody TaskRequestDTO request) {
        TaskResponseDTO response = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Detalhes da tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @Operation(summary = "Atualizar tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> updateById(@PathVariable Long id, @RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateById(id, request));
    }

    @Operation(summary = "Deletar tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Alterar status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da tarefa atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }


    @Operation(summary = "Atribuir usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atribuido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/tasks/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignUser(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.assignUser(id, userId));
    }

}
