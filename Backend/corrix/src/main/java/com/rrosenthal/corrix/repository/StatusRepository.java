package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface StatusRepository extends JpaRepository<Status, UUID> {
    Optional<Status> findByNameIgnoreCase(String name);
}
