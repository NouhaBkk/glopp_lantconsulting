package com.example.lantconsulting.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre personnalisé pour restreindre l'accès aux endpoints en fonction de accountname.
 */
public class AccountNameAuthorizationFilter extends OncePerRequestFilter {

    private final String requiredAccountName;

    public AccountNameAuthorizationFilter(String requiredAccountName) {
        this.requiredAccountName = requiredAccountName;
    }

    /**
     * Filtre les requêtes HTTP entrantes pour s'assurer que l'utilisateur authentifié a le nom de compte requis.
     * Si l'utilisateur est authentifié et que son nom d'utilisateur ne correspond pas au nom de compte requis,
     * une erreur 403 Forbidden est renvoyée. Sinon, la requête est autorisée à continuer.
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @param filterChain la chaîne de filtres
     * @throws ServletException si une erreur survient pendant le filtrage
     * @throws IOException si une erreur d'E/S survient pendant le filtrage
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifie si l'utilisateur est authentifié
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Récupère le username (accountname)

            // Si le username n'est pas "admin", renvoyer une erreur 403
            if (!requiredAccountName.equals(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
        }

        // Si tout est correct, continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
