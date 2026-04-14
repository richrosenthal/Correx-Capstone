package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ActionItemRepository extends JpaRepository<ActionItem, UUID> {
    List<ActionItem> findByCapa_Id(UUID capaId);

    @Query("""
            select count(a)
            from ActionItem a
            where a.dueDate < :today
              and a.completedDate is null
              and (a.status is null or lower(a.status.name) not in ('closed', 'done', 'completed'))
            """)
    long countOverdueActionItems(@Param("today") LocalDate today);

    @Query("""
            select count(a)
            from ActionItem a
            where a.dueDate < :cutoff
              and a.completedDate is null
              and (a.status is null or lower(a.status.name) not in ('closed', 'done', 'completed'))
            """)
    long countEscalatedActionItems(@Param("cutoff") LocalDate cutoff);
}
