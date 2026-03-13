package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.UserRequestDTO;
import com.josveronez.desafio_astentask.business.dto.UserResponseDTO;
import com.josveronez.desafio_astentask.business.dto.UserUpdateDTO;
import com.josveronez.desafio_astentask.business.exceptions.ConflictException;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.enums.UserRole;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("save deve criar usuário quando email não existe")
    void save_deveCriarUsuario_quandoEmailNaoExiste() {
        UserRequestDTO request = new UserRequestDTO("João", "joao@email.com", "senha123", UserRole.DEVELOPER);
        User user = new User();
        user.setId(1L);
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword("encoded");
        user.setRole(request.role());

        when(userRepository.findUserByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("João");
        assertThat(result.email()).isEqualTo("joao@email.com");
        assertThat(result.role()).isEqualTo(UserRole.DEVELOPER);
        verify(userRepository).findUserByEmail(request.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("save deve lançar ConflictException quando email já existe")
    void save_deveLancarConflict_quandoEmailJaExiste() {
        UserRequestDTO request = new UserRequestDTO("João", "joao@email.com", "senha123", UserRole.DEVELOPER);
        User existente = new User();
        existente.setEmail("joao@email.com");

        when(userRepository.findUserByEmail(request.email())).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> userService.save(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("já está em uso");
        verify(userRepository).findUserByEmail(request.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("findById deve retornar usuário quando existe")
    void findById_deveRetornarUsuario_quandoExiste() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setName("Maria");
        user.setEmail("maria@email.com");
        user.setRole(UserRole.ADMIN);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.email()).isEqualTo("maria@email.com");
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando não existe")
    void findById_deveLancarNotFound_quandoNaoExiste() {
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Não encontramos nenhum usuário");
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("updateById deve atualizar nome quando request traz nome")
    void updateById_deveAtualizarNome_quandoRequestTrazNome() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setName("Antigo");
        user.setEmail("user@email.com");
        user.setRole(UserRole.DEVELOPER);
        UserUpdateDTO request = new UserUpdateDTO("Novo Nome", null, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDTO result = userService.updateById(id, request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Novo Nome");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("deleteById deve deletar quando usuário existe")
    void deleteById_deveDeletar_quandoExiste() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);

        userService.deleteById(id);

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteById deve lançar ResourceNotFoundException quando não existe")
    void deleteById_deveLancarNotFound_quandoNaoExiste() {
        Long id = 999L;
        when(userRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrado");
        verify(userRepository, never()).deleteById(any());
    }
}