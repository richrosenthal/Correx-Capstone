package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceTypeRepository extends JpaRepository<SourceType, UUID>{
    Optional<SourceType> findByNameIgnoreCase(String name);
}
