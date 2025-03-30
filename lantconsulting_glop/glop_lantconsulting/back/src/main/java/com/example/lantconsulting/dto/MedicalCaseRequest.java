package com.example.lantconsulting.dto;

public class MedicalCaseRequest {
    private Long userId;   // ID du client (patient)
    private Long doctorId; 
    private String diagnosis;
    private String treatmentPlan;
    private String message;
  
    public MedicalCaseRequest() {}

    public MedicalCaseRequest(Long userId, Long doctorId, String diagnosis, String treatmentPlan) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.message = message;
    }

    // Getters et Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }
    public String getMessage() { 
        return message;
    }

    public void setMessage(String message) { 
        this.message = message;
    }
}
