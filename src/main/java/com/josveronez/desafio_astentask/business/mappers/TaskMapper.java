package com.josveronez.desafio_astentask.business.mappers;

import com.josveronez.desafio_astentask.business.dto.TaskRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TaskResponseDTO;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.User;

public class TaskMapper {

    public static Task toEntity(TaskRequestDTO request, Project project, User assignee, User reporter) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setEstimatedHours(request.estimatedHours());
        task.setDueDate(request.dueDate());
        task.setProject(project);
        task.setAssignee(assignee);
        task.setReporter(reporter);

        return task;
    }

    public static TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProject().getName(),
                task.getAssignee() != null ? task.getAssignee().getName() : "Sem assignee",
                task.getReporter().getName(),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getDueDate(),
                task.getCreatedAt()
        );
    }

}
