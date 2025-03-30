package com.example.lantconsulting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "child") // Nom de la table en base de données
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String gender;
    private int age;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // ✅ **Ajout du constructeur demandé**
    public Child(String firstName, String lastName, String gender, int age, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.account = account;
    }

    // ✅ **Ajout d'un constructeur sans paramètre pour JPA**
    public Child() {}

    // ✅ Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
