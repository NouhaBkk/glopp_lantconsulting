package com.example.lantconsulting.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users") 
public class User extends Account {

    private String civility;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String email;
    private String address;
    private String phoneNumber;
    private String maritalStatus;
    private boolean motorized;
    private String vehicleName;
    private int annualTravelPercentage;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children;

    @ManyToOne
    @JoinColumn(name = "conseiller_id")
    @JsonBackReference
    private Conseiller conseiller;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;



    public User() {
        super();
        this.setRoles(new HashSet<>()); // Initialise la liste des rôles
        this.getRoles().add("ROLE_USER"); // Ajoute le rôle spécifique
    }

    public void setConseiller(Conseiller conseiller) {
        this.conseiller = conseiller;
        conseiller.getClients().add(this);
    }

    public Conseiller getConseiller() {
        return conseiller;
    }
    

	
	 public String getCivility() {
	        return civility;
	    }

    public void setCivility(String civility) {
        this.civility = civility;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
    public boolean isMotorized() {
        return motorized;
    }

    public void setMotorized(boolean motorized) {
        this.motorized = motorized;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getAnnualTravelPercentage() {
        return annualTravelPercentage;
    }

    public void setAnnualTravelPercentage(int annualTravelPercentage) {
        this.annualTravelPercentage = annualTravelPercentage;
    }
    
    

	
}
