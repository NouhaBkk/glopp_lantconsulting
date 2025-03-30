package com.example.lantconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lantconsulting.entity.Contract;
import com.example.lantconsulting.entity.Offer;
import com.example.lantconsulting.entity.User;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByUser_Id(Long userId); // Trouver par l'ID de l'utilisateur
    List<Contract> findByUser_Accountname(String accountname); // Trouver par le nom de l'utilisateur
    List<Contract> findByUserId(Long userId);
    List<Contract> findByUserAndOffer(User user, Offer offer);

}
