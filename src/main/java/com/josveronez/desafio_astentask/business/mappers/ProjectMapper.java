package com.josveronez.desafio_astentask.business.mappers;

import com.josveronez.desafio_astentask.business.dto.ProjectRequestDTO;
import com.josveronez.desafio_astentask.business.dto.ProjectResponseDTO;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;

public class ProjectMapper {

    public static Project toEntity(ProjectRequestDTO request, User owner) {
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setOwner(owner);

        // Se status vier nulo, status default é pending
        project.setStatus(request.status() != null ? request.status() : ProjectStatus.PENDING);


        return project;
    }

    public static ProjectResponseDTO toResponseDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                //garantir que get owner não seja nulo antes de chamar getid
                project.getOwner() != null ? project.getOwner().getId() : null
        );
    }

}
