package com.josveronez.desafio_astentask.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(SecurityFilter securityFilter, UserDetailsService userDetailsService) {
        this.securityFilter = securityFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // admin gerencia usuarios
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // admin e project_manager gerenciam projetos
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")

                        // todos visualizam projetos
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")

                        //listar tarefas de projeto e detalhes da tarefa - qualquer usuario autenticado
                        .requestMatchers(HttpMethod.GET,  "/api/projects/*/tasks","/api/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")

                        //admin project manager e developer gerenciam tarefas
                        .requestMatchers(HttpMethod.POST, "/api/projects/*/tasks").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")

                        //listar comentarios de tarefa - qualquer usuario autenticado
                        .requestMatchers(HttpMethod.GET, "/api/tasks/*/comments").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")

                        //gerenciar comentarios - admin projectmanager e developer
                        .requestMatchers(HttpMethod.POST, "/api/tasks/*/comments").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")

                        //listar timelogs - qualquer autenticado
                        .requestMatchers(HttpMethod.GET, "/api/tasks/*/timelogs").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")

                        //gerenciar timelogs - admin projectmanager e developer
                        .requestMatchers(HttpMethod.POST, "/api/tasks/*/timelogs").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.PUT, "/api/timelogs/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.DELETE, "/api/timelogs/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")

                        //dashboard - qualquer autenticado
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")

                        //outras rotas
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}