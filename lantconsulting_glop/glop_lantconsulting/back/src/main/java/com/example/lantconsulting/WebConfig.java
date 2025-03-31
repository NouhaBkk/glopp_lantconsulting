package com.example.lantconsulting;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.micrometer.common.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // Applique CORS aux endpoints API
        .allowedOrigins(    "https://lantconsulting-k7n8.onrender.com",   
                            "http://localhost",     
                            "http://localhost:3000",
                            "http://localhost:80", 
                            "http://127.0.0.1")
 // Autorise uniquement le front React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autorise ces méthodes HTTP
                .allowedHeaders("*") // Autorise tous les headers
                .allowCredentials(true); // Permet l'envoi de cookies et des identifiants
    }
}





