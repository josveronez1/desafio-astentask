package com.josveronez.desafio_astentask.domain.repositories;

import com.josveronez.desafio_astentask.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByProjectIdAndStatus(Long projectId, String status);


}
