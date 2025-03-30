package com.example.lantconsulting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lantconsulting.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findByRegion(String region);
    Optional<Partner> findByAccountname(String accountname);
	Optional<Partner> findByEmail(String email);
	Optional<Partner> findFirstByRegionIgnoreCase(String region);
}
