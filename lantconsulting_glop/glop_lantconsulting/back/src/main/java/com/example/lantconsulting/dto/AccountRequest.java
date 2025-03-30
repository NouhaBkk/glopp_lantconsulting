package com.example.lantconsulting.dto;

import java.time.LocalDate;
import java.util.List;

public class AccountRequest {
    private String accountname;
    private String password;
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
    private List<ChildRequest> children;

    // Getters et Setters

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<ChildRequest> getChildren() {
        return children;
    }

    public void setChildren(List<ChildRequest> children) {
        this.children = children;
    }
}
