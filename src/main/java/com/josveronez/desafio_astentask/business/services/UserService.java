package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.dto.UserRequestDTO;
import com.josveronez.desafio_astentask.business.dto.UserResponseDTO;
import com.josveronez.desafio_astentask.business.dto.UserUpdateDTO;
import com.josveronez.desafio_astentask.business.exceptions.ConflictException;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.business.mappers.UserMapper;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO save(UserRequestDTO requestDTO){
        if (userRepository.findUserByEmail(requestDTO.email()).isPresent()){
            throw new ConflictException("O email " + requestDTO.email() + "já está em uso.");
        }
        User user = UserMapper.toEntity(requestDTO);

        user.setPassword(this.passwordEncoder.encode(requestDTO.password()));

        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO findByEmail(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum usuário com esse email.")
        );
        return UserMapper.toResponseDTO(user);
    }

    public UserResponseDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum usuário com esse id.")
        );
        return UserMapper.toResponseDTO(user);
    }

    public Page<UserResponseDTO> findAll(
            Pageable pageable
    ) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toResponseDTO);
    }

    // Atualiza usuário + mantém os campos, caso vierem nulos (ex: se atualizar só o email, muda o email e mantem os outros campos.)
    public UserResponseDTO updateById(Long id, UserUpdateDTO request){
        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Nenhum usuário encontrado com esse id.")
        );
        if (request.name() != null){
            userToUpdate.setName(request.name());
        }
        // Verifica se o novo email não está em uso.
        if (request.email() != null && !request.email().equals(userToUpdate.getEmail())){
            if (userRepository.findUserByEmail(request.email()).isPresent()){
                throw new ConflictException("Este email já está sendo utilizado por outro usuário.");
            }
            userToUpdate.setEmail(request.email());
        }
        if (request.role() != null) {
            userToUpdate.setRole(request.role());
        }

        if (request.password() != null && !request.password().isBlank()) {
            userToUpdate.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = userRepository.save(userToUpdate);
        return UserMapper.toResponseDTO(updatedUser);
    }

    public void deleteById(Long id){
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Usuário com ID" + id + "não encontrado.");
        }
        userRepository.deleteById(id);
    }

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));
    }
}
