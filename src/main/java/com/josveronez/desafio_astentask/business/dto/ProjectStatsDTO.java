package com.josveronez.desafio_astentask.business.dto;

public record ProjectStatsDTO(
        Long projectId,
        String projectName,
        Long totalTasks,
        long pendingTasks,
        long inProgressTasks,
        long completedTasks,
        double totalHoursLogged

) {
}
