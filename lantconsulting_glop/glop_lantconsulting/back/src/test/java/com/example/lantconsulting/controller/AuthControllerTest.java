//// filepath: src/test/java/com/example/lantconsulting/controller/AuthControllerTest.java
//package com.example.lantconsulting.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.example.lantconsulting.dto.AccountRequest;
//import com.example.lantconsulting.service.AuthService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@WebMvcTest(AuthController.class)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    @Test
//    public void testRegister() throws Exception {
//        // Créer une demande d'inscription
//        AccountRequest accountRequest = new AccountRequest();
//        accountRequest.setAccountname("testuser");
//        accountRequest.setPassword("password");
//
//        // Convertir la demande en JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//        String accountRequestJson = objectMapper.writeValueAsString(accountRequest);
//
//        // Simuler le comportement de register dans AuthService
//        // En fonction de ce que tu veux tester, ajuster la réponse
//        String mockResponse = "{\"message\":\"Compte créé avec succès !\"}";
//
//        // Tester l'endpoint /register
//        mockMvc.perform(post("/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(accountRequestJson))
//                .andExpect(status().isOk())  // Vérifier que le status est 200 OK
//                .andExpect(content().json(mockResponse));  // Vérifier le contenu de la réponse
//    }
//}
