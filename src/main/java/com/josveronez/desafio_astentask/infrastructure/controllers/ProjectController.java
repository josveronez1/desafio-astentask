package com.josveronez.desafio_astentask.infrastructure.controllers;


import com.josveronez.desafio_astentask.business.dto.ProjectRequestDTO;
import com.josveronez.desafio_astentask.business.dto.ProjectResponseDTO;
import com.josveronez.desafio_astentask.business.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos")
public class ProjectController {

    private final ProjectService projectService;
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }


    @Operation(summary = "Listar projetos do usuário")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> findByOwnerId(@RequestParam Long userId) {
        return ResponseEntity.ok(projectService.findByOwnerId(userId));
    }

    @Operation(summary = "Criar projeto")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> save(@RequestBody ProjectRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.save(request));
    }

    @Operation(summary = "Detalhes do projeto")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @Operation(summary = "Atualizar projeto")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateById(@PathVariable Long id, @RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = projectService.updateById(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar projeto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Falta criar estatísticas
}
