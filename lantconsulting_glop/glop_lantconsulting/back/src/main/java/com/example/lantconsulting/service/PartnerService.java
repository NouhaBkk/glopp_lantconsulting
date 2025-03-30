package com.example.lantconsulting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.lantconsulting.entity.Partner;
import com.example.lantconsulting.repository.PartnerRepository;

@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PartnerService(PartnerRepository partnerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.partnerRepository = partnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Partner createPartner(Partner partner) {
        // Vérifie si le mot de passe est déjà hashé
        if (!partner.getPassword().startsWith("$2a$")) {
            partner.setPassword(passwordEncoder.encode(partner.getPassword()));
        }

        return partnerRepository.save(partner);
    }
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll(); 
    }
    public Optional<Partner> getPartnerByRegion(String region) {
        return partnerRepository.findFirstByRegionIgnoreCase(region);
    }

}
