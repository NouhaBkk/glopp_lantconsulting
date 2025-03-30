package com.example.lantconsulting.dto;

import java.time.LocalDate;
import com.example.lantconsulting.entity.MedicalCaseStatus;

public class MedicalCaseResponse {
    private Long id;
    private Long clientId;  
    private String firstName;
    private String lastName;
    private String diagnosis;
    private MedicalCaseStatus status;
    private String message;
    private LocalDate createdDate; 

    
    public MedicalCaseResponse(Long id, Long clientId, String firstName, String lastName, 
                               String diagnosis, MedicalCaseStatus status, String message, LocalDate createdDate) {
        this.id = id;
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.diagnosis = diagnosis;
        this.status = status;
        this.message = message;
        this.createdDate = createdDate; // ✅ Initialisation correcte
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() { 
        return clientId;
    }

    public void setClientId(Long clientId) { 
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public MedicalCaseStatus getStatus() {
        return status;
    }

    public void setStatus(MedicalCaseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
