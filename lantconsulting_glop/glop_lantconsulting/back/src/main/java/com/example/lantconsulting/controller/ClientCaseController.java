package com.example.lantconsulting.controller;

import com.example.lantconsulting.dto.ClientCaseRequest;
import com.example.lantconsulting.entity.CaseDocument;
import com.example.lantconsulting.entity.ClientCase;
import com.example.lantconsulting.entity.Partner;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.repository.ClientCaseRepository;
import com.example.lantconsulting.repository.PartnerRepository;
import com.example.lantconsulting.repository.UserRepository;
import com.example.lantconsulting.service.CloudinaryService;
import com.example.lantconsulting.service.CountryRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientcases")
public class ClientCaseController {

    @Autowired private ClientCaseRepository clientCaseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PartnerRepository partnerRepository;
    @Autowired private CountryRegionService countryRegionService;
    @Autowired private CloudinaryService cloudinaryService;

    @PostMapping("/create")
    public ResponseEntity<?> createClientCase(@RequestBody ClientCaseRequest request) {
        try {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé");
            }

            String region = countryRegionService.getRegionFromCountry(request.getCountry());
            Optional<Partner> partnerOpt = partnerRepository.findFirstByRegionIgnoreCase(region);

            if (partnerOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Aucun partenaire trouvé pour la région: " + region);
            }

            ClientCase clientCase = new ClientCase();
            clientCase.setUser(userOpt.get());
            clientCase.setPartner(partnerOpt.get());
            clientCase.setAssistanceType(request.getAssistanceType());
            clientCase.setDescription(request.getDescription());
            clientCase.setStatus("En attente");
            clientCase.setCreationDate(LocalDate.now());

            clientCaseRepository.save(clientCase);
            return ResponseEntity.ok(clientCase);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erreur serveur : " + e.getMessage());
        }
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<?> uploadDocumentForCase(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("sender") String sender // "PARTNER" ou "CLIENT"
    ) {
        try {
            ClientCase clientCase = clientCaseRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier non trouvé"));

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
            String fileUrl = (String) uploadResult.get("url");
            String originalFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);

            // Création du document enrichi
            CaseDocument caseDocument = new CaseDocument();
            caseDocument.setUrl(fileUrl + "?name=" + originalFilename);
            caseDocument.setFileName(file.getOriginalFilename());
            caseDocument.setSender(sender.toUpperCase());
            caseDocument.setUploadedAt(java.time.LocalDateTime.now());
            caseDocument.setViewed(false);

            // Ajout à la liste
            clientCase.getDocuments().add(caseDocument);
            clientCaseRepository.save(clientCase);

            return ResponseEntity.ok(Map.of(
                    "message", "Document ajouté avec succès",
                    "url", caseDocument.getUrl(),
                    "fileName", caseDocument.getFileName(),
                    "sender", caseDocument.getSender()
            ));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de l'envoi du document"));
        }
    }

    
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ClientCase>> getClientCasesByUserId(@PathVariable Long userId) {
        List<ClientCase> clientCases = clientCaseRepository.findByUserId(userId);
        return ResponseEntity.ok(clientCases);
    }
    
    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getDocumentsForCase(@PathVariable Long id) {
        ClientCase clientCase = clientCaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier non trouvé"));
        return ResponseEntity.ok(clientCase.getDocuments());
    }
    
    @PutMapping("/{caseId}/documents/mark-viewed")
    public ResponseEntity<?> markDocumentViewed(@PathVariable Long caseId, @RequestBody Map<String, String> payload) {
        String url = payload.get("url");
        String viewerRole = payload.get("viewerRole");

        ClientCase clientCase = clientCaseRepository.findById(caseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier non trouvé"));

        Optional<CaseDocument> optionalDoc = clientCase.getDocuments().stream()
            .filter(doc -> doc.getUrl().equals(url))
            .findFirst();

        if (optionalDoc.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvé");
        }

        CaseDocument document = optionalDoc.get();

        if (!document.getSender().equalsIgnoreCase(viewerRole) && !document.isViewed()) {
            document.setViewed(true);
            clientCaseRepository.save(clientCase);
        }

        return ResponseEntity.ok(Map.of("message", "Document marqué comme vu"));
    }







}
