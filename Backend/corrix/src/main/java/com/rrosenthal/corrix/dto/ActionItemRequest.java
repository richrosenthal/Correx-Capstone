package com.rrosenthal.corrix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class ActionItemRequest {

    @NotNull
    private UUID capaId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    private UUID statusId;

    private UUID ownerId;

    private UUID assigneeId;

    @NotNull
    private LocalDate dueDate;

    private LocalDate completedDate;

    @Size(max = 4000)
    private String evidenceNotes;

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
    public UUID getStatusId() {
        return statusId;
    }
    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }
    public UUID getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
    public UUID getAssigneeId() {
        return assigneeId;
    }
    public void setAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
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
}
