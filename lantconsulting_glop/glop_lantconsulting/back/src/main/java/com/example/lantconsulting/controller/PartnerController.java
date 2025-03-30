package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.ClientCase;
import com.example.lantconsulting.entity.Partner;
import com.example.lantconsulting.repository.ClientCaseRepository;
import com.example.lantconsulting.service.PartnerService;
import com.example.lantconsulting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;
    private final UserService userService;
    private final ClientCaseRepository clientCaseRepository;

    // ✅ Constructeur mis à jour pour injecter clientCaseRepository
    public PartnerController(PartnerService partnerService, UserService userService, ClientCaseRepository clientCaseRepository) {
        this.partnerService = partnerService;
        this.userService = userService;
        this.clientCaseRepository = clientCaseRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPartner(@Valid @RequestBody Partner partner) {
        if (partner.getAccountname() == null || partner.getAccountname().isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur est obligatoire.");
        }
        if (partner.getFirstName() == null || partner.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().body("Le prénom est obligatoire.");
        }
        if (partner.getLastName() == null || partner.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom est obligatoire.");
        }
        if (partner.getEmail() == null || partner.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("L'email est obligatoire.");
        }
        if (partner.getCountry() == null || partner.getCountry().isEmpty()) {
            return ResponseEntity.badRequest().body("Le pays est obligatoire.");
        }
        if (partner.getRegion() == null || partner.getRegion().isEmpty()) {
            return ResponseEntity.badRequest().body("La région est obligatoire.");
        }

        Partner createdPartner = partnerService.createPartner(partner);
        return ResponseEntity.ok(createdPartner);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Partner>> getAllPartners() {
        List<Partner> partners = partnerService.getAllPartners();
        if (partners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-region")
    public ResponseEntity<?> getPartnerByRegion(@RequestParam String region) {
        return partnerService.getPartnerByRegion(region)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Aucun partenaire trouvé pour cette région"));
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getCasesForConnectedPartner(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).body("Non autorisé : utilisateur non connecté");
        }
        System.out.println("🔐 Utilisateur connecté : " + principal.getName());

        List<ClientCase> cases = userService.getClientCasesByPartnerAccountname(principal.getName());
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/cases/by-accountname")
    public ResponseEntity<?> getCasesByAccountname(@RequestParam String accountname) {
        List<ClientCase> cases = userService.getClientCasesByPartnerAccountname(accountname);
        return ResponseEntity.ok(cases);
    }

    @PutMapping("/clientcases/update/{id}")
    public ResponseEntity<?> updateCaseStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        ClientCase clientCase = clientCaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cas non trouvé"));

        clientCase.setStatus(newStatus);
        clientCaseRepository.save(clientCase);

        return ResponseEntity.ok(clientCase);
    }
}
