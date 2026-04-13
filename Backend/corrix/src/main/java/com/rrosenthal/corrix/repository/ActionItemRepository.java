package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActionItemRepository extends JpaRepository<ActionItem, UUID> {
    List<ActionItem> findByCapa_Id(UUID capaId);
}
