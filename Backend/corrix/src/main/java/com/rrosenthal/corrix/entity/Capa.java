package com.rrosenthal.corrix.entity;

import jakarta.persistence.*;
import jakarta.persistence.PreUpdate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "capas")
public class Capa extends WorkItem {

    @Column(nullable = false, unique = true, length =50)
    private String capaNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CapaStage stage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "severity_id")
    private Severity severity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_type_id")
    private SourceType sourceType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(length = 100)
    private String rootCauseCategory;

    @Column
    private Boolean effectivenessPassed;

    @Column
    private OffsetDateTime closedAt;

    @PrePersist
    public void prePersist(){
        if (getId() == null){
            setId(UUID.randomUUID());
        }
        if (stage == null) {
            stage = CapaStage.DRAFT;
        }
        OffsetDateTime now =  OffsetDateTime.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(){
        setUpdatedAt(OffsetDateTime.now());
    }
    public String getCapaNumber() {
        return capaNumber;
    }
    public void setCapaNumber(String capaNumber) {
        this.capaNumber = capaNumber;
    }
    public CapaStage getStage() {
        return stage;
    }
    public void setStage(CapaStage stage) {
        this.stage = stage;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Severity getSeverity() {
        return severity;
    }
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    public SourceType getSourceType() {
        return sourceType;
    }
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String getRootCauseCategory() {
        return rootCauseCategory;
    }
    public void setRootCauseCategory(String rootCauseCategory) {
        this.rootCauseCategory = rootCauseCategory;
    }
    public Boolean getEffectivenessPassed() {
        return effectivenessPassed;
    }
    public void setEffectivenessPassed(Boolean effectivenessPassed) {
        this.effectivenessPassed = effectivenessPassed;
    }
    public OffsetDateTime getClosedAt() {
        return closedAt;
    }
    public void setClosedAt(OffsetDateTime closedAt) {
        this.closedAt = closedAt;
    }

}
