package com.josveronez.desafio_astentask.business.services;

import com.josveronez.desafio_astentask.business.exceptions.ConflictException;
import com.josveronez.desafio_astentask.business.exceptions.ResourceNotFoundException;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()){
            throw new ConflictException("O email " + user.getEmail() + "já está em uso.");
        }
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum usuário com esse email.")
        );
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Não encontramos nenhum usuário com esse id.")
        );
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    // Atualiza usuário + mantém os campos, caso vierem nulos (ex: se atualizar só o email, muda o email e mantem os outros campos.)
    public User updateById(Long id, User user){
        User userToUpdate = findById(id);
        if (user.getName() != null){
            userToUpdate.setName(user.getName());
        }
        // Verifica se o novo email não está em uso.
        if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())){
            if (userRepository.findUserByEmail(user.getEmail()).isPresent()){
                throw new ConflictException("Este email já está sendo utilizado por outro usuário.");
            }
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            userToUpdate.setRole(user.getRole());
        }
        return userRepository.save(userToUpdate);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }






}
