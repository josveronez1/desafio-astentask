package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.ProjectRequestDTO;
import com.josveronez.desafio_astentask.business.dto.ProjectResponseDTO;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.domain.entities.Project;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.ProjectStatus;
import com.josveronez.desafio_astentask.domain.repositories.ProjectRepository;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private com.josveronez.desafio_astentask.domain.repositories.TaskRepository taskRepository;

    @Mock
    private com.josveronez.desafio_astentask.domain.repositories.CommentRepository commentRepository;

    @Mock
    private com.josveronez.desafio_astentask.domain.repositories.TimeLogRepository timeLogRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("findById deve retornar projeto quando existe")
    void findById_deveRetornarProjeto_quandoExiste() {
        Long id = 1L;
        User owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        Project project = new Project();
        project.setId(id);
        project.setName("Projeto A");
        project.setDescription("Desc");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setOwner(owner);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        ProjectResponseDTO result = projectService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Projeto A");
        assertThat(result.status()).isEqualTo(ProjectStatus.IN_PROGRESS);
        verify(projectRepository).findById(id);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando não existe")
    void findById_deveLancarNotFound_quandoNaoExiste() {
        Long id = 999L;
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Não encontramos nenhum projeto");
        verify(projectRepository).findById(id);
    }

    @Test
    @DisplayName("deleteById deve deletar quando projeto existe")
    void deleteById_deveDeletar_quandoExiste() {
        Long id = 1L;
        when(projectRepository.existsById(id)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(id);

        projectService.deleteById(id);

        verify(projectRepository).existsById(id);
        verify(projectRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteById deve lançar ResourceNotFoundException quando não existe")
    void deleteById_deveLancarNotFound_quandoNaoExiste() {
        Long id = 999L;
        when(projectRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> projectService.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");
        verify(projectRepository, never()).deleteById(any());
    }
}