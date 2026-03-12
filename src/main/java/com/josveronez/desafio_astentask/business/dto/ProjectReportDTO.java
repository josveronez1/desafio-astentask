package com.josveronez.desafio_astentask.business.dto;

import com.josveronez.desafio_astentask.domain.enums.TaskStatus;

import java.util.List;

public record ProjectReportDTO(
        Long projectId,
        String projectName,
        String projectStatus,
        long totalTasks,
        long totalComments,
        double totalHoursLogged,
        List<TaskSummaryDTO> tasks
) {
    public record TaskSummaryDTO(
            Long id,
            String title,
            TaskStatus status,
            String assigneeName,
            int commentCount,
            double hoursLogged
    ) { }
}
