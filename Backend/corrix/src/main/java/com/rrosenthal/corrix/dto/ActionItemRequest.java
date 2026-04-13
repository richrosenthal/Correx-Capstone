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

    private UUID assigneeId;

    private LocalDate dueDate;

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
}
