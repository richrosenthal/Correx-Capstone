package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface SeverityRepository extends JpaRepository<Severity, UUID> {
    Optional<Severity> findByNameIgnoreCase(String name);
}
