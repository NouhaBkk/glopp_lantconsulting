package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.Admin;
import com.example.lantconsulting.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Admin> getAllAdmins() {
        return accountService.getAllAccounts()
                             .stream()
                             .filter(account -> account instanceof Admin)
                             .map(account -> (Admin) account)
                             .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = accountService.getAccountById(id)
                                              .filter(account -> account instanceof Admin)
                                              .map(account -> (Admin) account);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
