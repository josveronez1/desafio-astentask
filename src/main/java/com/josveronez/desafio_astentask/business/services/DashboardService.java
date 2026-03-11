package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.DashboardOverviewDTO;
import com.josveronez.desafio_astentask.business.dto.MyTasksDTO;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import com.josveronez.desafio_astentask.domain.repositories.ProjectRepository;
import com.josveronez.desafio_astentask.domain.repositories.TaskRepository;
import com.josveronez.desafio_astentask.domain.repositories.TimeLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final TaskRepository taskRepository;
    private final TimeLogRepository timeLogRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public DashboardService(TaskRepository taskRepository,
                            TimeLogRepository timeLogRepository,
                            ProjectRepository projectRepository,
                            UserService userService) {
        this.taskRepository = taskRepository;
        this.timeLogRepository = timeLogRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public DashboardOverviewDTO getOverview() {
        User user = userService.getAuthenticatedUser();
        Long userId = user.getId();

        long totalTasks = taskRepository.countByAssigneeId(userId);
        long pending = taskRepository.countByAssigneeIdAndStatus(userId, TaskStatus.PENDING);
        long inProgress = taskRepository.countByAssigneeIdAndStatus(userId, TaskStatus.IN_PROGRESS);
        long completed = taskRepository.countByAssigneeIdAndStatus(userId, TaskStatus.COMPLETED);

        Double totalHours = timeLogRepository.sumHoursWorkedByUserId(userId);
        if (totalHours == null){
            totalHours = 0.0;
        }

        long projectsOwned = projectRepository.countByOwnerId(userId);

        return new DashboardOverviewDTO(totalTasks, pending, inProgress, completed, totalHours, projectsOwned);
    }

    public Page<MyTasksDTO> getMyTasks(Pageable pageable) {
        User user = userService.getAuthenticatedUser();

        return taskRepository.findByAssigneeId(user.getId(), pageable)
                .map(task -> new MyTasksDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getDueDate()
                ));
    }

}
