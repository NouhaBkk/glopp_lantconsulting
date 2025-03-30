package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.Contract;
import com.example.lantconsulting.entity.Offer;
import com.example.lantconsulting.entity.PdfService;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.service.ContractService;
import com.example.lantconsulting.service.OfferService;
import com.example.lantconsulting.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.lantconsulting.repository.ContractRepository;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private OfferService offerService;
    
    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private ContractRepository contractRepository;


    //  Récupérer tous les contrats
    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    //  Récupérer un contrat par ID
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contract = contractService.getContractById(id);
        return contract.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Ajouter un contrat
    @PostMapping("/add")
    public ResponseEntity<Contract> addContract(@RequestBody Contract contract) {
        Contract savedContract = contractService.saveContract(contract);
        return ResponseEntity.ok(savedContract);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToOffer(@RequestBody Map<String, Object> request) {
        if (!request.containsKey("offerId") || !request.containsKey("accountname")) {
            return ResponseEntity.badRequest().body("Paramètres 'offerId' et 'accountname' requis.");
        }

        Long offerId = Long.valueOf(request.get("offerId").toString());
        String accountname = request.get("accountname").toString();

        User user = userService.getUserByAccountname(accountname)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Offer offer = offerService.getOfferById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        List<Contract> existingContracts = contractRepository.findByUserAndOffer(user, offer);
        if (!existingContracts.isEmpty()) {
            return ResponseEntity.badRequest().body("Vous avez déjà souscrit à cette offre.");
        }

        Double finalPrice = request.containsKey("finalPrice") ?
                Double.valueOf(request.get("finalPrice").toString()) :
                offer.getPrice();

        Contract contract = new Contract();
        contract.setUser(user);
        contract.setOffer(offer);
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusMonths(12));
        contract.setFinalPrice(finalPrice);

        contractService.saveContract(contract);
        return ResponseEntity.ok(contract);
    }



    //  Supprimer un contrat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{contractId}/pdf")
    public ResponseEntity<byte[]> downloadContractPdf(@PathVariable Long contractId) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);

        if (contractOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Contract contract = contractOpt.get();
        String offerTitle = contract.getOffer().getTitle();
        double price = contract.getFinalPrice() != null ? contract.getFinalPrice() : contract.getOffer().getPrice();

        String accountname = contract.getUser().getAccountname(); // ✅ via l'utilisateur
        String dateDebut = contract.getStartDate().toString();
        String dateFin = contract.getEndDate().toString();

        ByteArrayInputStream pdfStream = pdfService.generateContractPdf(offerTitle, accountname, price, dateDebut, dateFin);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=contrat_" + contractId + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }
    @GetMapping("/user/{userId}")
    public List<Contract> getContractsByUser(@PathVariable Long userId) {
        return contractService.getContractsByUserId(userId);
    }

}