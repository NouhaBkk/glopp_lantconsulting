package com.example.lantconsulting.service;

import com.example.lantconsulting.dto.AccountRequest;
import com.example.lantconsulting.entity.Child;
import com.example.lantconsulting.entity.ClientCase;
import com.example.lantconsulting.entity.Conseiller;
import com.example.lantconsulting.entity.Partner;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.repository.ClientCaseRepository;
import com.example.lantconsulting.repository.ConseillerRepository;
import com.example.lantconsulting.repository.PartnerRepository;
import com.example.lantconsulting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ConseillerRepository conseillerRepository;
    @Autowired private PartnerRepository partnerRepository;
    @Autowired private ClientCaseRepository clientCaseRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Optional<User> findByAccountname(String accountname) {
        return userRepository.findByAccountname(accountname);
    }

    public Optional<User> getUserByAccountname(String accountname) {
        return userRepository.findByAccountname(accountname);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<ClientCase> getClientCasesByPartnerAccountname(String accountname) {
        Optional<Partner> partnerOpt = partnerRepository.findByAccountname(accountname);
        if (partnerOpt.isEmpty()) return Collections.emptyList();
        return clientCaseRepository.findByPartner(partnerOpt.get());
    }

    public User createUser(AccountRequest accountRequest) {
        User newUser = new User();
        newUser.setAccountname(accountRequest.getAccountname());
        newUser.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        newUser.setRoles(Set.of("ROLE_USER"));
        newUser.setCivility(accountRequest.getCivility());
        newUser.setLastName(accountRequest.getLastName());
        newUser.setFirstName(accountRequest.getFirstName());
        newUser.setBirthDate(accountRequest.getBirthDate());
        newUser.setEmail(accountRequest.getEmail());
        newUser.setAddress(accountRequest.getAddress());
        newUser.setPhoneNumber(accountRequest.getPhoneNumber());
        newUser.setMaritalStatus(accountRequest.getMaritalStatus());
        newUser.setMotorized(accountRequest.isMotorized());
        newUser.setVehicleName(accountRequest.getVehicleName());
        newUser.setAnnualTravelPercentage(accountRequest.getAnnualTravelPercentage());

        // Gestion des enfants
        newUser.setChildren(accountRequest.getChildren().stream().map(childRequest -> 
            new Child(
                childRequest.getFirstName(),
                childRequest.getLastName(),
                childRequest.getGender(),
                childRequest.getAge(),
                newUser
            )
        ).collect(Collectors.toList()));

        // Attribution automatique du conseiller
        Conseiller conseillerAvecMoinsClients = conseillerRepository.findAll().stream()
                .min(Comparator.comparingInt(Conseiller::getNumberOfClients))
                .orElse(null);

        if (conseillerAvecMoinsClients != null) {
            newUser.setConseiller(conseillerAvecMoinsClients);
        }

        return userRepository.save(newUser);
    }
}
