package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.Conseiller;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.service.ConseillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/conseillers")
public class ConseillerController {

    @Autowired
    private ConseillerService conseillerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Récupérer tous les conseillers
    @GetMapping
    public ResponseEntity<List<Conseiller>> getAllConseillers() {
        List<Conseiller> conseillers = conseillerService.getAllConseillers();
        return ResponseEntity.ok(conseillers);
    }

    // ✅ Récupérer un conseiller par ID
    @GetMapping("/{id}")
    public ResponseEntity<Conseiller> getConseillerById(@PathVariable Long id) {
        return conseillerService.getConseillerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Récupérer un conseiller par `accountname`
    @GetMapping("/mon-profile")
    public ResponseEntity<?> getMonProfile(@RequestParam String accountname) {
        Optional<Conseiller> conseiller = conseillerService.findByAccountname(accountname);

        if (conseiller.isPresent()) {
            return ResponseEntity.ok(conseiller.get());
        } else {
            return ResponseEntity.status(404).body("Conseiller introuvable");
        }
    }

    // ✅ Ajouter un conseiller avec vérification des rôles
    @PostMapping("/add")
    public ResponseEntity<?> addConseiller(@RequestBody Conseiller conseiller) {
        try {
            // Vérifier que l'accountname est unique
            if (conseillerService.findByAccountname(conseiller.getAccountname()).isPresent()) {
                return ResponseEntity.badRequest().body("Ce conseiller existe déjà.");
            }

            // Encoder le mot de passe
            conseiller.setPassword(passwordEncoder.encode(conseiller.getPassword()));

            // S'assurer que le rôle est bien ajouté
            if (conseiller.getRoles() == null) {
                conseiller.setRoles(new HashSet<>());
            }
            conseiller.getRoles().add("ROLE_CONSEILLER");

            Conseiller savedConseiller = conseillerService.saveConseiller(conseiller);
            return ResponseEntity.ok(savedConseiller);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout du conseiller : " + e.getMessage());
        }
    }

    // ✅ Supprimer un conseiller par ID avec vérification
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConseiller(@PathVariable Long id) {
        Optional<Conseiller> conseiller = conseillerService.getConseillerById(id);
        if (conseiller.isEmpty()) {
            return ResponseEntity.status(404).body("Conseiller introuvable.");
        }

        conseillerService.deleteConseiller(id);
        return ResponseEntity.ok("Conseiller supprimé avec succès.");
    }

    // ✅ Récupérer les clients d’un conseiller
    @GetMapping("/{id}/clients")
    public ResponseEntity<?> getConseillerClients(@PathVariable Long id) {
        Optional<Conseiller> conseiller = conseillerService.getConseillerById(id);
        if (conseiller.isEmpty()) {
            return ResponseEntity.status(404).body("Conseiller introuvable.");
        }

        List<User> clients = conseiller.get().getClients();
        if (clients == null || clients.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(clients);
    }
}
