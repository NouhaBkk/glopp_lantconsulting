package com.example.lantconsulting.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor extends Account {

    public Doctor() {
        super();
        // Ajout du rôle spécifique pour le médecin
        this.getRoles().add("ROLE_DOCTOR");
    }
}
