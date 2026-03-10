package com.josveronez.desafio_astentask.business.mappers;

import com.josveronez.desafio_astentask.business.dto.TimeLogRequestDTO;
import com.josveronez.desafio_astentask.business.dto.TimeLogResponseDTO;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.entities.TimeLog;
import com.josveronez.desafio_astentask.domain.entities.User;

public class TimeLogMapper {

    public static TimeLog toEntity(TimeLogRequestDTO request, Task task, User user) {
        TimeLog timelog = new TimeLog();
        timelog.setHoursWorked(request.hoursWorked());
        timelog.setDescription(request.description());
        timelog.setLogDate(request.logDate());
        timelog.setTask(task);
        timelog.setUser(user);
        return timelog;
    }

    public static TimeLogResponseDTO toResponseDTO(TimeLog timelog) {
        return new TimeLogResponseDTO(
                timelog.getId(),
                timelog.getTask() != null ? timelog.getTask().getId() : null,
                timelog.getUser() != null ? timelog.getUser().getId() : null,
                timelog.getUser() != null ? timelog.getUser().getName() : "Usuário desconhecido",
                timelog.getHoursWorked(),
                timelog.getDescription() != null ? timelog.getDescription() : "Sem descrição",
                timelog.getLogDate()

        );
    }

}
