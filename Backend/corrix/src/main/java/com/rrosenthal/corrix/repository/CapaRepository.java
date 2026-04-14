package com.rrosenthal.corrix.repository;

import com.rrosenthal.corrix.entity.Capa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface CapaRepository extends JpaRepository<Capa, UUID>  {

    List<Capa> findByTitleContainingIgnoreCase(String title);
    List<Capa> findByCapaNumberContainingIgnoreCase(String capaNumber);

    @Query("""
            select count(c)
            from Capa c
            where (c.stage is null or c.stage <> com.rrosenthal.corrix.entity.CapaStage.CLOSED)
              and (c.status is null or lower(c.status.name) <> 'closed')
            """)
    long countOpenCapas();

    @Query("""
            select count(c)
            from Capa c
            where c.closedAt >= :start
              and c.closedAt < :end
            """)
    long countClosedCapasBetween(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);

    @Query("""
            select count(c)
            from Capa c
            where c.dueDate < :today
              and (c.stage is null or c.stage <> com.rrosenthal.corrix.entity.CapaStage.CLOSED)
              and (c.status is null or lower(c.status.name) <> 'closed')
            """)
    long countOverdueCapas(@Param("today") LocalDate today);

    @Query("""
            select count(c)
            from Capa c
            where c.dueDate < :cutoff
              and (c.stage is null or c.stage <> com.rrosenthal.corrix.entity.CapaStage.CLOSED)
              and (c.status is null or lower(c.status.name) <> 'closed')
            """)
    long countEscalatedCapas(@Param("cutoff") LocalDate cutoff);

    @Query("""
            select count(c)
            from Capa c
            where c.dueDate >= :start
              and c.dueDate <= :end
              and (c.stage is null or c.stage <> com.rrosenthal.corrix.entity.CapaStage.CLOSED)
              and (c.status is null or lower(c.status.name) <> 'closed')
            """)
    long countDueSoonCapas(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
            select count(c)
            from Capa c
            where c.closedAt is not null
            """)
    long countClosedCapas();

    @Query("""
            select count(c)
            from Capa c
            where c.effectivenessPassed = true
            """)
    long countEffectivenessPassed();

    @Query("""
            select count(c)
            from Capa c
            where c.effectivenessPassed is not null
            """)
    long countEffectivenessEvaluated();

    List<Capa> findByClosedAtIsNotNull();

    @Query("""
            select c.sourceType.name as label, count(c) as count
            from Capa c
            group by c.sourceType.name
            order by count(c) desc, c.sourceType.name asc
            """)
    List<DashboardCountProjection> countCapasBySourceType();

    @Query("""
            select coalesce(c.rootCauseCategory, 'Unspecified') as label, count(c) as count
            from Capa c
            group by coalesce(c.rootCauseCategory, 'Unspecified')
            order by count(c) desc, coalesce(c.rootCauseCategory, 'Unspecified') asc
            """)
    List<DashboardCountProjection> countCapasByRootCauseCategory();
}
