package com.example.lantconsulting.entity.mocks;

import com.example.lantconsulting.entity.Account;

import jakarta.persistence.Entity;

import java.util.HashSet;
import java.util.Set;

@Entity
public class AccountMock extends Account {

    public AccountMock() {
        super("mockAccount", "mockPassword", new HashSet<>(Set.of("ROLE_USER")));
    }

    public AccountMock(String accountname, String password, Set<String> roles) {
        super(accountname, password, roles);
    }
}