package com.example.lantconsulting.service;

import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.entity.Doctor;
import com.example.lantconsulting.entity.MedicalCase;
import com.example.lantconsulting.repository.UserRepository;
import com.example.lantconsulting.repository.DoctorRepository;
import com.example.lantconsulting.repository.MedicalCaseRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalCaseService {

    @Autowired
    private MedicalCaseRepository medicalCaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public MedicalCase saveMedicalCase(MedicalCase medicalCase) {
        if (medicalCase.getClient() == null || medicalCase.getClient().getId() == null) {
            throw new IllegalArgumentException("Erreur : L'ID du client est requis pour créer un dossier médical.");
        }

        // Vérifier que le médecin n'est pas null et existe
        if (medicalCase.getDoctor() == null || medicalCase.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Le médecin est requis");
        }

        // Récupérer les entités persistantes
        User client = userRepository.findById(medicalCase.getClient().getId())
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        Doctor doctor = doctorRepository.findById(medicalCase.getDoctor().getId())
            .orElseThrow(() -> new RuntimeException("Docteur non trouvé"));

        // Attacher les entités
        medicalCase.setClient(client);
        medicalCase.setDoctor(doctor);
        
        return medicalCaseRepository.save(medicalCase);
    }
 // Ajouter la méthode pour récupérer les dossiers d'un médecin
    public List<MedicalCase> getMedicalCasesByDoctorId(Long doctorId) {
        return medicalCaseRepository.findByDoctorId(doctorId);
    }

    // Ajouter la méthode pour récupérer les dossiers d'un patient
    public List<MedicalCase> getMedicalCasesByUserId(Long userId) {
        return medicalCaseRepository.findByClientId(userId);
    }
}
