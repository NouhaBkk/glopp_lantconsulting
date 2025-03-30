package com.example.lantconsulting.repository;

import com.example.lantconsulting.entity.Conseiller;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConseillerRepository extends JpaRepository<Conseiller, Long> {
	Optional<Conseiller> findByAccountname(String accountname); 
}
	