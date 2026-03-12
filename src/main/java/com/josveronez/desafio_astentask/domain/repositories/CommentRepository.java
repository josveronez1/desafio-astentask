package com.josveronez.desafio_astentask.domain.repositories;

import com.josveronez.desafio_astentask.domain.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTaskId(Long taskId);

    Page<Comment> findByTaskId(Long taskId, Pageable pageable);

    Long countByTaskId(Long taskId);


}
