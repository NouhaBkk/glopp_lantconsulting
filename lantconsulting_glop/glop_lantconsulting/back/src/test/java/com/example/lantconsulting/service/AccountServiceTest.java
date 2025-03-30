//package com.example.lantconsulting.service;
//
//import java.util.Optional;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.example.lantconsulting.entity.Account;
//import com.example.lantconsulting.entity.mocks.AccountMock;
//import com.example.lantconsulting.repository.AccountRepository;
//
//@SpringBootTest
//class AccountServiceTest {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    void testCreateAccount() {
//        Account account = new AccountMock();
//        account.setAccountname("testuser");
//        account.setPassword(passwordEncoder.encode("password123"));
//        account.setRoles(Set.of("ROLE_USER"));
//        accountRepository.save(account);
//
//        Optional<Account> foundAccount = accountRepository.findByAccountname("testuser");
//        assertNotNull(foundAccount);
//        assertTrue(passwordEncoder.matches("password123", foundAccount.get().getPassword()));
//    }
//}
