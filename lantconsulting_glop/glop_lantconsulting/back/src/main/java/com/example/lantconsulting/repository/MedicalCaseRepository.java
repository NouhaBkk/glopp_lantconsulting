package com.example.lantconsulting.repository;

import com.example.lantconsulting.entity.MedicalCase;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("medicalcaseRepositoryUnique")
public interface MedicalCaseRepository extends JpaRepository<MedicalCase, Long> {

    List<MedicalCase> findByDoctorId(Long doctorId);

    List<MedicalCase> findByClientId(Long clientId);
}
