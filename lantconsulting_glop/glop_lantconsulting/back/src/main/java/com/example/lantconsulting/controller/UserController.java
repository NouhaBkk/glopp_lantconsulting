package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<User> getAllAccounts() {
        return accountService.getAllUsers()
                             .stream()
                             .filter(account -> account instanceof User)
                             .map(account -> (User) account)
                             .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = accountService.getAccountById(id)
                                            .filter(account -> account instanceof User)
                                            .map(account -> (User) account);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    
}
