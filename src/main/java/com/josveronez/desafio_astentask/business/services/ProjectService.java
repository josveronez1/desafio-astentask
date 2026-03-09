package com.josveronez.desafio_astentask.business.services;


import com.josveronez.desafio_astentask.business.dto.ProjectRequestDTO;
import com.josveronez.desafio_astentask.business.dto.ProjectResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.ProjectMapper;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.repositories.ProjectRepository;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService (ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDTO save(ProjectRequestDTO request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Usuário dono do projeto não encontrado")
                );

        Project project = ProjectMapper.toEntity(request, owner);

        return ProjectMapper.toResponseDTO(projectRepository.save(project));
    }

    public ProjectResponseDTO findById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum projeto com esse id.")
        );
        return ProjectMapper.toResponseDTO(project);
    }

    public List<ProjectResponseDTO> findByOwnerId(Long userId) {
        //validar se userid existe
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }

        return projectRepository.findByOwnerId(userId)
                .stream()
                .map(ProjectMapper::toResponseDTO)
                .toList();
    }

    public ProjectResponseDTO updateById(Long id, ProjectRequestDTO request) {
        Project projectToUpdate = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Nenhum projeto com esse id.")
        );

        if (request.name() != null){
            projectToUpdate.setName(request.name());
        }

        if (request.description() != null){
            projectToUpdate.setDescription(request.description());
        }

        if (request.status() != null){
            projectToUpdate.setStatus(request.status());
        }

        if (request.ownerId() != null){
            User newOwner = userRepository.findById(request.ownerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com esse id."));
            projectToUpdate.setOwner(newOwner);
        }

        Project updatedProject = projectRepository.save(projectToUpdate);
        return ProjectMapper.toResponseDTO(updatedProject);
    }

    public void deleteById(Long id){
        if (!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Projeto com ID" + id + "não encontrado.");
        }
        projectRepository.deleteById(id);
    }


}
