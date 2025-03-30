package com.example.lantconsulting.entity;

public enum MedicalCaseStatus {
    EN_ATTENTE("EN_ATTENTE"),
    EN_COURS_DE_TRAITEMENT("EN_COURS_DE_TRAITEMENT"),
    TERMINE("TERMINE");

    private final String value;

    MedicalCaseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Permet de convertir une String en Enum
    public static MedicalCaseStatus fromString(String status) {
        for (MedicalCaseStatus s : MedicalCaseStatus.values()) {
            if (s.value.equalsIgnoreCase(status.replace(" ", "_"))) {
                return s;
            }
        }
        throw new IllegalArgumentException("Statut inconnu : " + status);
    }
}
