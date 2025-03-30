package com.example.lantconsulting.dto;

import java.time.LocalDate;

public class ContractRequest {

    private String details; // Les détails du contrat
    private LocalDate startDate; // La date de début du contrat
    private LocalDate endDate; // La date de fin du contrat
    private UserDTO user; // L'utilisateur associé au contrat

    // Getters et Setters
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // Classe imbriquée pour les informations de l'utilisateur
    public static class UserDTO {
        private String accountname; // Nom du compte de l'utilisateur

        // Getters et Setters
        public String getAccountname() {
            return accountname;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }
    }
}
