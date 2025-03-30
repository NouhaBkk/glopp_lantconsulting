package com.example.lantconsulting.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "conseillers")
public class Conseiller extends Account { // Hérite de Account


    @OneToMany(mappedBy = "conseiller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<User> clients; 

    public Conseiller() {
        super(); // Appelle le constructeur de Account
        this.setRoles(new HashSet<>()); // Initialise la liste des rôles
        this.getRoles().add("ROLE_CONSEILLER"); // Ajoute le rôle spécifique
    }

    

    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        this.clients = clients;
    }
    
    public int getNumberOfClients() {
        return (clients != null) ? clients.size() : 0;
    }
}
