package com.example.lantconsulting.service;

import com.example.lantconsulting.entity.Account;
import com.example.lantconsulting.entity.Admin;
import com.example.lantconsulting.entity.Conseiller;
import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> authenticate(String accountname, String password) {
        Optional<Account> accountOpt = accountRepository.findByAccountname(accountname);

        if (accountOpt.isEmpty() || !passwordEncoder.matches(password, accountOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Identifiants invalides"));
        }

        Account account = accountOpt.get();
        String role;
        
        if (account instanceof User) {
            role = "USER";
        } else if (account instanceof Admin) {
            role = "ADMIN";
        } else if (account instanceof Conseiller) {
            role = "CONSEILLER";
        } else {
            role = "UNKNOWN";
        }

        return ResponseEntity.ok(Map.of(
                "username", account.getAccountname(),
                "role", role
        ));
    }

}
