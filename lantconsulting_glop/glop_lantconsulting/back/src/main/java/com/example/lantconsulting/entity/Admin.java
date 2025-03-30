package com.example.lantconsulting.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends Account {

    public Admin() {
        super(); // Appelle le constructeur de Account
        this.setRoles(new HashSet<>()); // Initialise la liste des rôles
        this.getRoles().add("ROLE_ADMIN"); // Ajoute le rôle spécifique
    }

    public Admin(String accountname, String password) {
        super(accountname, password, Set.of("ROLE_ADMIN"));
    }
}
