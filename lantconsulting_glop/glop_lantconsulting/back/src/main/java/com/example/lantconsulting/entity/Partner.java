package com.example.lantconsulting.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

@Entity
@Table(name = "partners")
public class Partner extends Account {

    @Column(nullable = false)
    @JsonProperty("firstName") // Assure que Spring récupère bien la valeur depuis le JSON
    private String firstName;

    @Column(nullable = false)
    @JsonProperty("lastName")
    private String lastName;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(nullable = false)
    @JsonProperty("country")
    private String country;

    @Column(nullable = false)
    @JsonProperty("region")
    private String region;

    public Partner() {
        super();
        this.setRoles(Set.of("ROLE_PARTNER"));
    }

    public Partner(String accountname, String password, String firstName, String lastName, String email, String country, String region) {
        super(accountname, password, Set.of("ROLE_PARTNER"));
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.region = region;
    }

    // Getters et Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
