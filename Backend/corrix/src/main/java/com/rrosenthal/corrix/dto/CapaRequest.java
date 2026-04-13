package com.rrosenthal.corrix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;


public class CapaRequest {

    @NotBlank
    @Size(max = 50)
    private String capaNumber;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    private UUID statusId;

    @NotNull
    private UUID severityId;

    @NotNull
    private UUID sourceTypeId;

    private UUID ownerId;

    private LocalDate dueDate;

    public String getCapaNumber() {
        return capaNumber;
    }
    public void setCapaNumber(String capaNumber) {
        this.capaNumber = capaNumber;
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
    public UUID getSeverityId() {
        return severityId;
    }
    public void setSeverityId(UUID severityId) {
        this.severityId = severityId;
    }
    public UUID getSourceTypeId() {
        return sourceTypeId;
    }
    public void setSourceTypeId(UUID sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }
    public UUID getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
