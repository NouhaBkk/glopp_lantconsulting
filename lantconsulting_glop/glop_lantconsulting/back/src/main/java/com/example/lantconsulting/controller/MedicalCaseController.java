package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.MedicalCase;
import com.example.lantconsulting.entity.MedicalCaseStatus;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.repository.MedicalCaseRepository;
import com.example.lantconsulting.entity.Doctor;
import com.example.lantconsulting.service.MedicalCaseService;
import com.example.lantconsulting.service.UserService;
import com.example.lantconsulting.service.DoctorService;
import com.example.lantconsulting.dto.MedicalCaseRequest; // Import correct ici
import com.example.lantconsulting.dto.MedicalCaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-cases")
public class MedicalCaseController {

	@Autowired
	private MedicalCaseService medicalCaseService;

	@Autowired
	private UserService userService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private MedicalCaseRepository medicalCaseRepository;

	/**
	 * API pour qu'un utilisateur contacte un médecin
	 */
	@PostMapping("/request")
	public ResponseEntity<String> requestMedicalCase(@RequestBody MedicalCaseRequest request) {
		try {
			System.out.println("Requête reçue : " + request);

			if (request.getUserId() == null || request.getDoctorId() == null) {
				return ResponseEntity.badRequest().body("Erreur : ID utilisateur ou médecin manquant.");
			}

			User user = userService.findById(request.getUserId()).orElseThrow(
					() -> new RuntimeException("Utilisateur introuvable avec ID : " + request.getUserId()));

			Doctor doctor = doctorService.getDoctorById(request.getDoctorId())
					.orElseThrow(() -> new RuntimeException("Médecin introuvable avec ID : " + request.getDoctorId()));

			MedicalCase medicalCase = new MedicalCase();
			medicalCase.setClient(user);
			medicalCase.setDoctor(doctor);
			medicalCase.setDiagnosis(request.getDiagnosis());
			medicalCase.setTreatmentPlan(request.getTreatmentPlan());

			medicalCase.setStatus(MedicalCaseStatus.EN_ATTENTE);
			medicalCase.setCreatedDate(LocalDate.now());
			medicalCase.setMessage(request.getMessage());
			medicalCaseService.saveMedicalCase(medicalCase);
			return ResponseEntity.ok("Demande envoyée au médecin !");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
		}
	}

	/**
	 * API pour qu'un médecin ajoute un dossier médical
	 */
	@PostMapping("/create")
	public ResponseEntity<?> createMedicalCase(@RequestBody MedicalCase medicalCase) {
		try {
			System.out.println(" Requête reçue pour ajout de dossier : " + medicalCase);

			if (medicalCase.getClient() == null || medicalCase.getClient().getId() == null) {
				System.err.println("Erreur : L'ID du client est manquant !");
				return ResponseEntity.badRequest().body(" Erreur : L'ID du client est requis.");
			}
			if (medicalCase.getDoctor() == null || medicalCase.getDoctor().getId() == null) {
				System.err.println("Erreur : L'ID du médecin est manquant !");
				return ResponseEntity.badRequest().body("Erreur : L'ID du médecin est requis.");
			}

			System.out.println(" ID du client reçu : " + medicalCase.getClient().getId());
			System.out.println("ID du médecin reçu : " + medicalCase.getDoctor().getId());

			User user = userService.findById(medicalCase.getClient().getId()).orElseThrow(
					() -> new RuntimeException("❌ Client non trouvé avec ID : " + medicalCase.getClient().getId()));

			// Vérifier si le médecin existe
			Doctor doctor = doctorService.getDoctorById(medicalCase.getDoctor().getId()).orElseThrow(
					() -> new RuntimeException("❌ Médecin non trouvé avec ID : " + medicalCase.getDoctor().getId()));

			// Vérification et conversion du statut
			if (medicalCase.getStatus() == null) {
				medicalCase.setStatus(MedicalCaseStatus.EN_COURS_DE_TRAITEMENT);
			} else {
				medicalCase.setStatus(MedicalCaseStatus.fromString(medicalCase.getStatus().toString()));
			}

			medicalCase.setCreatedDate(LocalDate.now());
			medicalCaseService.saveMedicalCase(medicalCase);

			System.out.println(" Dossier médical ajouté avec succès !");
			Map<String, String> response = new HashMap<>();
			response.put("message", "Dossier médical ajouté avec succès !");
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			System.err.println(" Statut invalide : " + e.getMessage());
			return ResponseEntity.badRequest().body(" Statut invalide : " + e.getMessage());
		} catch (RuntimeException e) {
			System.err.println(" Erreur : " + e.getMessage());
			return ResponseEntity.status(404).body(e.getMessage()); // Renvoie l'erreur exacte au frontend
		} catch (Exception e) {
			System.err.println("Erreur serveur : " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(500).body(" Erreur serveur : " + e.getMessage());
		}
	}

	/**
	 * Récupérer tous les dossiers médicaux pour un médecin
	 */
	@GetMapping("/doctor/{doctorId}")
	public List<MedicalCaseResponse> getMedicalCasesByDoctor(@PathVariable Long doctorId) {
		List<MedicalCase> cases = medicalCaseRepository.findByDoctorId(doctorId);
		return cases.stream()
				.map(medicalCase -> new MedicalCaseResponse(medicalCase.getId(), medicalCase.getClient().getId(),
						medicalCase.getClient().getFirstName(), medicalCase.getClient().getLastName(),
						medicalCase.getDiagnosis(), medicalCase.getStatus(), medicalCase.getMessage(),
						medicalCase.getCreatedDate()))
				.collect(Collectors.toList());
	}

	/**
	 * Récupérer tous les dossiers médicaux d'un patient
	 */
	@GetMapping("/user/{userId}")
	public List<MedicalCase> getMedicalCasesByUser(@PathVariable Long userId) {
		return medicalCaseService.getMedicalCasesByUserId(userId);
	}
}
