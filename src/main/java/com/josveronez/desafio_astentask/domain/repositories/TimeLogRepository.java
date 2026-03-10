package com.josveronez.desafio_astentask.domain.repositories;

import com.josveronez.desafio_astentask.domain.entities.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByTaskId(Long taskId);
}
