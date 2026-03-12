package com.josveronez.desafio_astentask.business.specifications;

import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.enums.TaskPriority;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecifications {

    public static Specification<Task> filterTasks(
            TaskStatus status,
            TaskPriority priority,
            Long assigneeId,
            Long projectId,
            LocalDateTime dueDateFrom,
            LocalDateTime dueDateTo
    ) {
        return Specification.where(hasStatus(status))
                .and(hasPriority(priority))
                .and(hasAssignee(assigneeId))
                .and(hasProject(projectId))
                .and(hasDueDateFrom(dueDateFrom))
                .and(hasDueDateTo(dueDateTo));
    }
    private static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
    private static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) ->
                priority == null ? null : cb.equal(root.get("priority"), priority);
    }
    private static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? null : cb.equal(root.get("assignee").get("id"), assigneeId);
    }
    private static Specification<Task> hasProject(Long projectId) {
        return (root, query, cb) ->
                projectId == null ? null : cb.equal(root.get("project").get("id"), projectId);
    }
    private static Specification<Task> hasDueDateFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("dueDate"), from);
    }
    private static Specification<Task> hasDueDateTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("dueDate"), to);
    }
}

