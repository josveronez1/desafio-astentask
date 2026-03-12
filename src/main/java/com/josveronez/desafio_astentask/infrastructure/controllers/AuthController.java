package com.josveronez.desafio_astentask.infrastructure.controllers;

import com.josveronez.desafio_astentask.business.dto.LoginRequestDTO;
import com.josveronez.desafio_astentask.business.dto.LoginResponseDTO;
import com.josveronez.desafio_astentask.business.dto.UserRequestDTO;
import com.josveronez.desafio_astentask.business.dto.UserResponseDTO;
import com.josveronez.desafio_astentask.business.services.UserService;
import com.josveronez.desafio_astentask.domain.entities.User;
import com.josveronez.desafio_astentask.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Registrar um usuário")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    @Operation(summary = "Realizar login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Renovar token (refresh)")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String oldToken = authorization.substring(7);
        String newToken = tokenService.refreshToken(oldToken);
        if (newToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new LoginResponseDTO(newToken));
    }

    @Operation(summary = "Logout (invalidação do token)")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            tokenService.revokeToken(token);
        }
        return ResponseEntity.noContent().build();
    }


}
