package com.rrosenthal.corrix.entity;
import jakarta.persistence.*;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "action_items")
public class ActionItem extends WorkItem {

    @ManyToOne(optional = false)
    @JoinColumn(name = "capa_id")
    private Capa capa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column
    private LocalDate completedDate;

    @Column(length = 4000)
    private String evidenceNotes;

    @PrePersist
    public void prePersist(){
        if (getId() == null){
            setId(UUID.randomUUID());
        }
        OffsetDateTime now =  OffsetDateTime.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(){
        setUpdatedAt(OffsetDateTime.now());
    }
    public Capa getCapa() {
        return capa;
    }
    public void setCapa(Capa capa) {
        this.capa = capa;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public User getAssignee() {
        return assignee;
    }
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
    public User getOwner() {
        return assignee;
    }
    public void setOwner(User owner) {
        this.assignee = owner;
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
