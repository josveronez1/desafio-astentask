package com.josveronez.desafio_astentask.business.services;


import com.josveronez.desafio_astentask.business.dto.*;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.ProjectMapper;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import com.josveronez.desafio_astentask.domain.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final TimeLogRepository timeLogRepository;

    public ProjectService (ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository, CommentRepository commentRepository, TimeLogRepository timeLogRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.timeLogRepository = timeLogRepository;
    }

    public ProjectResponseDTO save(ProjectRequestDTO request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Usuário dono do projeto não encontrado")
                );

        Project project = ProjectMapper.toEntity(request, owner);

        return ProjectMapper.toResponseDTO(projectRepository.save(project));
    }

    public ProjectResponseDTO findById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum projeto com esse id.")
        );
        return ProjectMapper.toResponseDTO(project);
    }

    public Page<ProjectResponseDTO> findByOwnerId(Long userId, Pageable pageable) {
        //validar se userid existe
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }
        return projectRepository.findByOwnerId(userId, pageable)
                .map(ProjectMapper::toResponseDTO);
    }

    public ProjectResponseDTO updateById(Long id, ProjectUpdateDTO request) {
        Project projectToUpdate = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Nenhum projeto com esse id.")
        );

        if (request.name() != null){
            projectToUpdate.setName(request.name());
        }

        if (request.description() != null){
            projectToUpdate.setDescription(request.description());
        }

        if (request.status() != null){
            projectToUpdate.setStatus(request.status());
        }

        if (request.ownerId() != null){
            User newOwner = userRepository.findById(request.ownerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com esse id."));
            projectToUpdate.setOwner(newOwner);
        }

        Project updatedProject = projectRepository.save(projectToUpdate);
        return ProjectMapper.toResponseDTO(updatedProject);
    }

    public void deleteById(Long id){
        if (!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Projeto com ID" + id + "não encontrado.");
        }
        projectRepository.deleteById(id);
    }

    public ProjectStatsDTO getProjectStats(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Nenhum projeto com esse id.")
        );
        long totalTasks = taskRepository.countByProjectId(projectId);
        long pendingTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.PENDING);
        long inProgressTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.IN_PROGRESS);
        long completedTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.COMPLETED);
        Double hours = timeLogRepository.sumHoursWorkedByProjectId(projectId);
        double totalHoursLogged = hours != null ? hours : 0.0;

        return new ProjectStatsDTO(project.getId(), project.getName(), totalTasks, pendingTasks, inProgressTasks, completedTasks, totalHoursLogged);
    }

    public ProjectReportDTO getProjectReport(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum projeto com esse id."));
        List<Task> tasks = taskRepository.findByProjectId(projectId, Pageable.unpaged()).getContent();
        long totalTasks = tasks.size();
        long totalComments = 0;
        List<ProjectReportDTO.TaskSummaryDTO> taskSummaries = new ArrayList<>();
        for (Task task : tasks) {
            Long commentCount = commentRepository.countByTaskId(task.getId());
            totalComments += (commentCount != null ? commentCount : 0);
            Double taskHours = timeLogRepository.sumHoursWorkedByTaskId(task.getId());
            double hoursLogged = taskHours != null ? taskHours : 0.0;
            String assigneeName = task.getAssignee() != null ? task.getAssignee().getName() : "Não atribuído";
            int comments = commentCount != null ? commentCount.intValue() : 0;
            taskSummaries.add(new ProjectReportDTO.TaskSummaryDTO(task.getId(), task.getTitle(), task.getStatus(), assigneeName, comments, hoursLogged
            ));
        }
        Double totalHoursLogged = timeLogRepository.sumHoursWorkedByProjectId(projectId);
        double totalHours = totalHoursLogged != null ? totalHoursLogged : 0.0;
        return new ProjectReportDTO(project.getId(), project.getName(), project.getStatus().name(), totalTasks, totalComments, totalHours, taskSummaries);
    }

}
