package com.example.lantconsulting.service;

import com.example.lantconsulting.entity.Conseiller;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.repository.ConseillerRepository;
import com.example.lantconsulting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ConseillerService {

    @Autowired
    private ConseillerRepository conseillerRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Récupérer tous les conseillers
    public List<Conseiller> getAllConseillers() {
        return conseillerRepository.findAll();
    }

    // ✅ Récupérer un conseiller par ID
    public Optional<Conseiller> getConseillerById(Long id) {
        return conseillerRepository.findById(id);
    }

    // ✅ Ajouter un conseiller
    public Conseiller saveConseiller(Conseiller conseiller) {
        return conseillerRepository.save(conseiller);
    }

    // ✅ Supprimer un conseiller par ID
    public void deleteConseiller(Long id) {
        conseillerRepository.deleteById(id);
    }

    // ✅ Trouver et attribuer le conseiller ayant le moins de clients
    public Optional<Conseiller> findConseillerWithLeastClients() {
        return conseillerRepository.findAll()
                .stream()
                .min(Comparator.comparingInt(c -> c.getClients().size()));
    }

    // ✅ Assigner un client à un conseiller disponible
    public void assignClientToConseiller(User user) {
        Optional<Conseiller> conseillerOpt = findConseillerWithLeastClients();
        conseillerOpt.ifPresent(conseiller -> {
            conseiller.getClients().add(user);
            user.setConseiller(conseiller);
            conseillerRepository.save(conseiller);
            userRepository.save(user);
        });
    }
    
    public Optional<Conseiller> findByAccountname(String accountname) {
        return conseillerRepository.findByAccountname(accountname);
    }
}
