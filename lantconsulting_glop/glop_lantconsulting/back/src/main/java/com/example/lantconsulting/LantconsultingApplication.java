package com.example.lantconsulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.lantconsulting.repository.AccountRepository;
import com.example.lantconsulting.repository.ContractRepository;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.example.lantconsulting.repository")
public class LantconsultingApplication {

    @Autowired
    private AccountRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LantconsultingApplication.class, args);
    }

    
}
