package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.TimeLogRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TimeLogResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.TimeLogMapper;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.TimeLog;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.repositories.TaskRepository;
import com.josveronez.desafio_astentask.domain.repositories.TimeLogRepository;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TimeLogService(TimeLogRepository timeLogRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.timeLogRepository = timeLogRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public TimeLogResponseDTO save(Long taskId, TimeLogRequestDTO request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada."));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        TimeLog timelog = TimeLogMapper.toEntity(request, task, user);

        return TimeLogMapper.toResponseDTO(timeLogRepository.save(timelog));
    }

    public Page<TimeLogResponseDTO> findByTaskId(Long taskId, Pageable pageable) {
        if(!taskRepository.existsById(taskId)){
            throw new ResourceNotFoundException("Tarefa não encontrada.");
        }

        return timeLogRepository.findByTaskId(taskId, pageable)
                .map(TimeLogMapper::toResponseDTO);
    }

    public TimeLogResponseDTO update(Long id, TimeLogRequestDTO request){
        TimeLog timeLogToUpdate = timeLogRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Registro não encontrado.")
                );
        if (request.description() != null){
            timeLogToUpdate.setDescription(request.description());
        }
        if (request.hoursWorked() != null){
            timeLogToUpdate.setHoursWorked(request.hoursWorked());
        }
        if (request.logDate() != null){
            timeLogToUpdate.setLogDate(request.logDate());
        }

        return TimeLogMapper.toResponseDTO(timeLogRepository.save(timeLogToUpdate));
    }

    public void deleteById(Long id){
        if (!timeLogRepository.existsById(id)){
            throw new ResourceNotFoundException("TimeLog com ID" + id + "não encontrado.");
        }
        timeLogRepository.deleteById(id);
    }

}
