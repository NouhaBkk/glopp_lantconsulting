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

    /**
     * Crée et configure un bean PasswordEncoder utilisant BCrypt.
     * 
     * @return un PasswordEncoder configuré avec BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la source de configuration CORS pour l'application.
     *
     * @return une instance de {@link CorsConfigurationSource} configurée avec les paramètres CORS spécifiés.
     *
     * La configuration CORS permet les origines suivantes :
     * - http://localhost:3000
     *
     * Les méthodes HTTP autorisées sont :
     * - GET
     * - POST
     * - PUT
     * - DELETE
     * - OPTIONS
     *
     * Tous les en-têtes sont autorisés et les informations d'identification sont permises.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(    "https://lantconsulting-k7n8.onrender.com",
                                                        "http://frontend",       
                                                    "http://localhost",   
                                                    "http://localhost:3000", 
                                                    "http://127.0.0.1"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configure la chaîne de filtres de sécurité pour l'application.
     *
     * <p>Cette méthode désactive la protection CSRF et configure les filtres CORS.
     * Elle définit également les règles d'autorisation pour les différentes routes de l'application :
     * <ul>
     *   <li>Autorise toutes les requêtes sur les chemins commençant par <code>/api/auth/**</code>.</li>
     *   <li>Autorise l'accès non authentifié aux chemins commençant par <code>/api/offers/**</code>.</li>
     *   <li>Autorise l'accès non authentifié aux chemins commençant par <code>/api/contracts/**</code> et <code>/api/users/**</code>.</li>
     *   <li>Autorise toutes les autres requêtes.</li>
     * </ul>
     * Enfin, elle configure l'authentification HTTP Basic.
     *
     * @param http l'objet {@link HttpSecurity} à configurer
     * @return l'objet {@link SecurityFilterChain} configuré
     * @throws Exception si une erreur survient lors de la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and()
            .authorizeRequests()
            .requestMatchers("/api/auth/**").permitAll() // Autorise toutes les requêtes sur /api/auth/**
            .requestMatchers("/api/offers/**").permitAll() // Autorise l'accès non authentifié aux offres
            .requestMatchers("/api/contracts/**", "/api/users/**").permitAll()// Requiert une authentification
            .requestMatchers("/api/medical-cases/**").permitAll()
            .anyRequest().permitAll() // Toutes les autres requêtes nécessitent une authentification
            .and()
            .httpBasic(); // Utilise l'authentification HTTP Basic

        return http.build();
    }
}
