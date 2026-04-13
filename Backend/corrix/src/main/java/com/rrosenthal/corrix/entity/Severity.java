package com.rrosenthal.corrix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "severities")
public class Severity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    //This will run before Hibernate saves a new Status to the database. If the id has not already been set
    //it automatically generates a new UUID.
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
