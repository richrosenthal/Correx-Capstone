package com.rrosenthal.corrix.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;


public class ActionItemResponse {

    private UUID id;
    private UUID capaId;
    private String title;
    private String description;
    private String status;
    private String owner;
    private String assignee;
    private LocalDate dueDate;
    private LocalDate completedDate;
    private String evidenceNotes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getCapaId() {
        return capaId;
    }
    public void setCapaId(UUID capaId) {
        this.capaId = capaId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getAssignee() {
        return assignee;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public LocalDate getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }
    public String getEvidenceNotes() {
        return evidenceNotes;
    }
    public void setEvidenceNotes(String evidenceNotes) {
        this.evidenceNotes = evidenceNotes;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
