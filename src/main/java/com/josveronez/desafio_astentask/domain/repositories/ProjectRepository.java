package com.josveronez.desafio_astentask.domain.repositories;

import com.josveronez.desafio_astentask.domain.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    //Achar todos os projetos do usuário pelo ownerId dos projetos / Id do usuario
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    Long countByOwnerId(Long ownerId);

}
