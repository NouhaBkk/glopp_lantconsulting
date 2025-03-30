package com.example.lantconsulting.controller;

import com.example.lantconsulting.dto.AccountRequest;
import com.example.lantconsulting.entity.*;
import com.example.lantconsulting.service.AccountService;
import com.example.lantconsulting.service.ConseillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConseillerService conseillerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountRequest accountRequest) {
        if (accountService.findByAccountname(accountRequest.getAccountname()).isPresent()) {
            return ResponseEntity.badRequest().body("Cet utilisateur existe déjà.");
        }

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

        List<Child> children = Optional.ofNullable(accountRequest.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .map(childRequest -> {
                    Child child = new Child();
                    child.setFirstName(childRequest.getFirstName());
                    child.setLastName(childRequest.getLastName());
                    child.setGender(childRequest.getGender());
                    child.setAge(childRequest.getAge());
                    child.setAccount(newUser);
                    return child;
                }).collect(Collectors.toList());

        newUser.setChildren(children);

        conseillerService.findConseillerWithLeastClients().ifPresent(newUser::setConseiller);

        accountService.saveAccount(newUser);
        return ResponseEntity.ok("Compte utilisateur créé avec succès !");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody AccountRequest accountRequest) {
        if (accountService.findByAccountname(accountRequest.getAccountname()).isPresent()) {
            return ResponseEntity.badRequest().body("Cet administrateur existe déjà.");
        }

        Admin newAdmin = new Admin();
        newAdmin.setAccountname(accountRequest.getAccountname());
        newAdmin.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        newAdmin.setRoles(Set.of("ROLE_ADMIN"));

        accountService.saveAccount(newAdmin);
        return ResponseEntity.ok("Compte administrateur créé avec succès !");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> loginData) {
        String accountname = loginData.get("accountname");
        String password = loginData.get("password");

        Optional<Account> accountOpt = accountService.findByAccountname(accountname);

        if (accountOpt.isEmpty() || !passwordEncoder.matches(password, accountOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Identifiants invalides"));
        }

        Account account = accountOpt.get();
        String role = account.getRoles().stream().findFirst().orElse("ROLE_USER");

        return ResponseEntity.ok(Map.of(
        		"id", account.getId(),
        		"username", account.getAccountname(),
                "role", role
        ));
    }
}
