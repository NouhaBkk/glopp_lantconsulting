package com.example.lantconsulting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_cases")
public class ClientCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'utilisateur qui a signalé l'incident

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner; // Le partenaire qui prend en charge le dossier

    private String assistanceType;
    private String description;
    private String status; // "En attente", "En cours", "Résolu"

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "client_case_documents",
        joinColumns = @JoinColumn(name = "client_case_id") 
    )
    private List<CaseDocument> documents = new ArrayList<>();


    
   @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    public ClientCase() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getAssistanceType() {
        return assistanceType;
    }

    public void setAssistanceType(String assistanceType) {
        this.assistanceType = assistanceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    public List<CaseDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<CaseDocument> documents) {
		this.documents = documents;
	}

	public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
