package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josveronez.desafio_astentask.business.dto.LoginRequestDTO;
import com.josveronez.desafio_astentask.business.dto.UserRequestDTO;
import com.josveronez.desafio_astentask.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/auth/register deve retornar 201 e usuário criado")
    void register_deveRetornar201_eUsuarioCriado() throws Exception {
        UserRequestDTO request = new UserRequestDTO(
                "Teste User",
                "teste.auth@email.com",
                "senha123",
                UserRole.DEVELOPER
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Teste User"))
                .andExpect(jsonPath("$.email").value("teste.auth@email.com"))
                .andExpect(jsonPath("$.role").value("DEVELOPER"));
    }

    @Test
    @DisplayName("POST /api/auth/login deve retornar 200 e token quando credenciais válidas")
    void login_deveRetornar200_eToken_quandoCredenciaisValidas() throws Exception {
        UserRequestDTO register = new UserRequestDTO("Login Test", "login.test@email.com", "senha123", UserRole.DEVELOPER);
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequestDTO login = new LoginRequestDTO("login.test@email.com", "senha123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}