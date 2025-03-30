package com.example.lantconsulting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl; // URL Cloudinary ou autre stockage

    @ManyToOne
    @JoinColumn(name = "client_case_id", nullable = false)
    private ClientCase clientCase;

    public Document() {
    }

    public Document(String fileName, String fileUrl, ClientCase clientCase) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.clientCase = clientCase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ClientCase getClientCase() {
        return clientCase;
    }

    public void setClientCase(ClientCase clientCase) {
        this.clientCase = clientCase;
    }
}
