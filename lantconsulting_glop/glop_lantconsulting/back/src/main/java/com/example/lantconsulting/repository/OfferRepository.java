package com.example.lantconsulting.repository;

import com.example.lantconsulting.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
