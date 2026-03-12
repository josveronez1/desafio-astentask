package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.ProjectReportDTO;
import com.josveronez.desafio_astentask.business.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Endpoints de relatórios")
public class ReportController {
    private final ProjectService projectService; // ou ReportService, se você criar
    public ReportController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @Operation(summary = "Relatório do projeto")
    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectReportDTO> getProjectReport(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectReport(id));
    }
}
