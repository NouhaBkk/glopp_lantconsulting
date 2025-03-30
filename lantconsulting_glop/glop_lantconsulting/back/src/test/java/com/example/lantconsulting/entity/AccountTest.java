//package com.example.lantconsulting.entity;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.example.lantconsulting.entity.mocks.AccountMock;
//
//@SpringBootTest
//class AccountTest {
//
//    @Test
//    void testAccountCreation() {
//        Account account = new AccountMock();
//        account.setAccountname("testuser");
//        account.setPassword("password123");
//        account.setRoles(Set.of("ROLE_USER"));
//
//        assertEquals("testuser", account.getAccountname());
//        assertEquals("password123", account.getPassword());
//        assertTrue(account.getRoles().contains("ROLE_USER"));
//    }
//}