package com.rrosenthal.corrix.dto;

import java.time.LocalDate;

public class AgingReportRowResponse {

    private String capaNumber;
    private String title;
    private String status;
    private String severity;
    private String owner;
    private LocalDate dueDate;
    private long daysOpen;

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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public long getDaysOpen() {
        return daysOpen;
    }
    public void setDaysOpen(long daysOpen) {
        this.daysOpen = daysOpen;
    }
}
