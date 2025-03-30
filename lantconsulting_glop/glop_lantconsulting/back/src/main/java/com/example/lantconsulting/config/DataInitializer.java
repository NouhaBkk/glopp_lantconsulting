package com.example.lantconsulting.config;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.lantconsulting.entity.Admin;
import com.example.lantconsulting.entity.Conseiller;
import com.example.lantconsulting.entity.Partner;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.entity.ClientCase;
import com.example.lantconsulting.repository.AccountRepository;
import com.example.lantconsulting.repository.ConseillerRepository;
import com.example.lantconsulting.repository.ClientCaseRepository;
import com.example.lantconsulting.repository.PartnerRepository;
import com.example.lantconsulting.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AccountRepository accountRepository, 
                                      PasswordEncoder passwordEncoder, 
                                      ConseillerRepository conseillerRepository,
                                      PartnerRepository partnerRepository,
                                      UserRepository userRepository,
                                      ClientCaseRepository clientCaseRepository) {
        return args -> {
            System.out.println("📌 Initialisation des données...");

            // 📌 Création de l'administrateur s'il n'existe pas
            if (accountRepository.findByAccountname("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setAccountname("admin");
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                accountRepository.save(admin);
                System.out.println("✅ Admin créé : admin / adminpassword");
            } else {
                System.out.println("ℹ️ Admin déjà existant.");
            }

//            // 📌 Vérification et création de l'utilisateur "sarahlo"
//            Optional<User> existingUser = userRepository.findByAccountname("sarahlo");
//            User client1;
//            if (existingUser.isEmpty()) {
//                client1 = new User();
//                client1.setAccountname("sarahlo");
//                client1.setPassword(passwordEncoder.encode("password"));
//                userRepository.save(client1);
//                System.out.println("✅ Utilisateur 'sarahlo' ajouté.");
//            } else {
//                client1 = existingUser.get();
//                System.out.println("ℹ️ Utilisateur 'sarahlo' existe déjà.");
//            }
//
//            // 📌 Création d'un conseiller "johndoe" s'il n'existe pas
//            if (accountRepository.findByAccountname("johndoe").isEmpty()) {
//                Conseiller conseiller = new Conseiller();
//                conseiller.setAccountname("johndoe");
//                conseiller.setPassword(passwordEncoder.encode("conseillerpassword"));
//                accountRepository.save(conseiller);
//                System.out.println("✅ Conseiller créé : johndoe / conseillerpassword");
//            } else {
//                System.out.println("ℹ️ Conseiller 'johndoe' existe déjà.");
//            }
//
//            // 📌 Vérification et création du partenaire "suzanne"
//            Optional<Partner> existingPartner = partnerRepository.findByAccountname("suzanne");
//            Partner suzanne;
//            if (existingPartner.isEmpty()) {
//                suzanne = new Partner();
//                suzanne.setAccountname("suzanne");
//                suzanne.setPassword(passwordEncoder.encode("partnerpass"));
//                partnerRepository.save(suzanne);
//                System.out.println("✅ Partenaire 'suzanne' ajouté.");
//            } else {
//                suzanne = existingPartner.get();
//                System.out.println("ℹ️ Partenaire 'suzanne' existe déjà.");
//            }
//
//            // 📌 Création d'un dossier client s'il n'existe pas déjà
//            if (clientCaseRepository.count() == 0) {
//                ClientCase dossier = new ClientCase();
//                dossier.setClient(client1);
//                dossier.setPartner(suzanne);
//                dossier.setAssistanceType("Dépannage");
//                dossier.setDescription("Panne de voiture sur l'autoroute");
//                dossier.setStatus("En attente");
//                dossier.setCreationDate(LocalDate.now()); // Date de création actuelle
//                clientCaseRepository.save(dossier);
//                System.out.println("✅ Dossier client créé.");
//            } else {
//                System.out.println("ℹ️ Un dossier client existe déjà.");
//            }
//
//            System.out.println("🎉 Données initialisées avec succès !");
       };
    }
}
