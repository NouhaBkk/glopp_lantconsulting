package com.example.lantconsulting.repository;

import com.example.lantconsulting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son nom de compte (username).
     * @param accountname Le nom d'utilisateur.
     * @return L'utilisateur correspondant s'il existe.
     */
    Optional<User> findByAccountname(String accountname);

    /**
     * Vérifie si un utilisateur existe avec ce nom de compte.
     * @param accountname Le nom d'utilisateur.
     * @return true si l'utilisateur existe, sinon false.
     */
    boolean existsByAccountname(String accountname);

    /**
     * Récupère tous les utilisateurs avec leurs contrats chargés.
     * @return La liste des utilisateurs.
     */
    List<User> findAll();
    
}
