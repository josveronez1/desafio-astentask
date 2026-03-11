package com.josveronez.desafio_astentask.domain.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.josveronez.desafio_astentask.domain.entities.Task;
import com.josveronez.desafio_astentask.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    List<Task> findByProjectIdAndStatus(Long projectId, String status);

    long countByAssigneeId(Long userId);

    long countByAssigneeIdAndStatus(Long userId, TaskStatus status);




}
