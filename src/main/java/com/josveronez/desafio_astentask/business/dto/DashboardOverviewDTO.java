package com.josveronez.desafio_astentask.business.dto;

public record DashboardOverviewDTO(
        long totalTasksAssigned,
        long totalPendingTasks,
        long inProgressTasks,
        long completedTasks,
        double totalHourLogged,
        long projectsOwnedCount

) {
}
