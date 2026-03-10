package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.TaskRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TaskResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.TaskMapper;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import com.josveronez.desafio_astentask.domain.repositories.ProjectRepository;
import com.josveronez.desafio_astentask.domain.repositories.TaskRepository;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponseDTO save(TaskRequestDTO request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado."));

        User reporter = userRepository.findById(request.reporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Relator não encontrado."));

        User assignee = userRepository.findById(request.assigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee não encontrado."));

        Task task = TaskMapper.toEntity(request, project, assignee, reporter);

        if (task.getStatus() != null){
            task.setStatus(TaskStatus.PENDING);
        }

        return TaskMapper.toResponseDTO(taskRepository.save(task));

    }

    public List<TaskResponseDTO> findAllByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Projeto não encontrado.");
        }
        return taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO findById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tarefa não encontrada."));
        return TaskMapper.toResponseDTO(task);
    }

    public TaskResponseDTO updateById(Long id, TaskRequestDTO request){
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tarefa não encontrada.")
                );
        if (request.title() != null){
            taskToUpdate.setTitle(request.title());
        }
        if (request.description() != null){
            taskToUpdate.setDescription(request.description());
        }
        if (request.priority() != null){
            taskToUpdate.setPriority(request.priority());
        }
        if (request.status() != null){
            taskToUpdate.setStatus(request.status());
        }
        return TaskMapper.toResponseDTO(taskRepository.save(taskToUpdate));
    }

    public TaskResponseDTO updateStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada."));
        task.setStatus(status);
        return TaskMapper.toResponseDTO(taskRepository.save(task));
    }

    public TaskResponseDTO assignUser(Long id, Long userId){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        task.setAssignee(user);
        return TaskMapper.toResponseDTO(taskRepository.save(task));
    }

    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)){
            throw new ResourceNotFoundException("Tarefa com ID" + id + "não encontrada.");
        }
        taskRepository.deleteById(id);
    }

}
