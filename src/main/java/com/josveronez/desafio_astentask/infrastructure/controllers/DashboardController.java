package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.DashboardOverviewDTO;
import com.josveronez.desafio_astentask.business.dto.MyTasksDTO;
import com.josveronez.desafio_astentask.business.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para visão do dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Visão geral do usuário")
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDTO> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }

    @Operation(summary = "Tarefas atribuídas ao usuário")
    @GetMapping("/my-tasks")
    public ResponseEntity<Page<MyTasksDTO>> getMyTasks(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(dashboardService.getMyTasks(pageable));
    }

}
