package com.example.lantconsulting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la source de configuration CORS pour l'application.
     *
     * @return une instance de {@link CorsConfigurationSource} configurée
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://lantconsulting-k7n8.onrender.com", // Frontend de Render
            "http://localhost:3000", // Environnement local de dev
            "http://127.0.0.1" // Environnement local de dev
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and() // Active CORS
            .authorizeRequests()
            .requestMatchers("/api/auth/**").permitAll() // Autorise /api/auth/** sans authentification
            .requestMatchers("/api/offers/**").permitAll() // Autorise /api/offers/** sans authentification
            .requestMatchers("/api/contracts/**", "/api/users/**").permitAll() // Permet l'accès non authentifié aux contrats et utilisateurs
            .requestMatchers("/api/medical-cases/**").permitAll()
            .anyRequest().authenticated() // Requiert une authentification pour toutes les autres requêtes
            .and()
            .httpBasic(); // Utilisation de l'authentification HTTP Basic

        return http.build();
    }
}
