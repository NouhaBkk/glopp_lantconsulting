//package com.example.lantconsulting.entity;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//class ChildTest {
//
//    @Test
//    void testChildSettersAndGetters() {
//        // Création du compte utilisateur (User)
//        Set<String> roles = new HashSet<>();
//        roles.add("ROLE_USER");
//
//        // Création d'un utilisateur avec un compte
//        User user = new User();
//        user.setAccountname("testAccount");
//        user.setPassword("password123");
//        user.setRoles(roles);  // Ajoute les rôles
//        user.setFirstName("John");
//        user.setLastName("Doe");
//
//        // Création d'un enfant et association du compte
//        Child child = new Child();
//        child.setFirstName("John");
//        child.setLastName("Doe");
//        child.setGender("M");
//        child.setAge(7);
//        child.setAccount(user);  // Association de l'enfant avec le compte de l'utilisateur
//
//        // Vérification des valeurs
//        assertEquals("John", child.getFirstName());
//        assertEquals("Doe", child.getLastName());
//        assertEquals("M", child.getGender());
//        assertEquals(7, child.getAge());
//        assertNotNull(child.getAccount());
//        assertEquals("testAccount", child.getAccount().getAccountname());
//        assertEquals("password123", child.getAccount().getPassword());
//    }
//
//    @Test
//    void testUserSettersAndGetters() {
//        // Création d'un utilisateur
//        Set<String> roles = new HashSet<>();
//        roles.add("ROLE_USER");
//        User user = new User();
//        user.setAccountname("testUser");
//        user.setPassword("password123");
//        user.setRoles(roles);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("john.doe@example.com");
//        user.setPhoneNumber("123-456-7890");
//
//        // Vérification des valeurs
//        assertEquals("testUser", user.getAccountname());
//        assertEquals("password123", user.getPassword());
//        assertEquals("ROLE_USER", user.getRoles().iterator().next());
//        assertEquals("John", user.getFirstName());
//        assertEquals("Doe", user.getLastName());
//        assertEquals("john.doe@example.com", user.getEmail());
//        assertEquals("123-456-7890", user.getPhoneNumber());
//    }
//}
