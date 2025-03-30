package com.example.lantconsulting.service;

import com.example.lantconsulting.entity.User;
import com.example.lantconsulting.entity.Account;
import com.example.lantconsulting.repository.AccountRepository;
import com.example.lantconsulting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Optional<Account> findByAccountname(String accountname) {
        return accountRepository.findByAccountname(accountname);
    }
}
