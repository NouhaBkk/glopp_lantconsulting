package com.example.lantconsulting.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class CaseDocument {

    private String url;
    private String fileName;
    private String sender; // "PARTNER" ou "CLIENT"
    private LocalDateTime uploadedAt;
    private boolean viewed = false;

    public CaseDocument() {}

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public boolean isViewed() { return viewed; }
    public void setViewed(boolean viewed) { this.viewed = viewed; }
}
