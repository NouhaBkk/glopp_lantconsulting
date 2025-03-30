package com.example.lantconsulting.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lantconsulting.entity.ClientCase;
import com.example.lantconsulting.entity.Partner;

@Repository
@Qualifier("clientCaseRepositoryUnique")
public interface ClientCaseRepository extends JpaRepository<ClientCase, Long> {

	List<ClientCase> findByPartner(Partner partner);
	List<ClientCase> findByUserId(Long userId);


}
