package com.josveronez.desafio_astentask.domain.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {


    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    long countByAssigneeId(Long userId);

    long countByAssigneeIdAndStatus(Long userId, TaskStatus status);




}
