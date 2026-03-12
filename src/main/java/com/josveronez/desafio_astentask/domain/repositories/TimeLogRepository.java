package com.josveronez.desafio_astentask.domain.repositories;

import com.josveronez.desafio_astentask.domain.entities.TimeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByTaskId(Long taskId);

    Page<TimeLog> findByTaskId(Long taskId, Pageable pageable);

    @Query("SELECT SUM(tl.hoursWorked) FROM TimeLog tl WHERE tl.user.id = :userId")
    Double sumHoursWorkedByUserId(Long userId);
}
