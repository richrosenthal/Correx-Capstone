package com.rrosenthal.corrix.service;

import com.rrosenthal.corrix.dto.DashboardBreakdownItemResponse;
import com.rrosenthal.corrix.dto.DashboardSummaryResponse;
import com.rrosenthal.corrix.entity.Capa;
import com.rrosenthal.corrix.repository.ActionItemRepository;
import com.rrosenthal.corrix.repository.CapaRepository;
import com.rrosenthal.corrix.repository.DashboardCountProjection;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DashboardService {

    private final CapaRepository capaRepository;
    private final ActionItemRepository actionItemRepository;

    public DashboardService(CapaRepository capaRepository, ActionItemRepository actionItemRepository) {
        this.capaRepository = capaRepository;
        this.actionItemRepository = actionItemRepository;
    }

    public DashboardSummaryResponse getSummary() {
        LocalDate today = LocalDate.now();
        LocalDate monthStartDate = today.withDayOfMonth(1);
        OffsetDateTime monthStart = monthStartDate.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        OffsetDateTime nextMonthStart = monthStartDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();

        long openCapasCount = capaRepository.countOpenCapas();
        long closedCapasThisMonth = capaRepository.countClosedCapasBetween(monthStart, nextMonthStart);
        long overdueCapaCount = capaRepository.countOverdueCapas(today);
        long overdueActionItemCount = actionItemRepository.countOverdueActionItems(today);

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setOpenCount(openCapasCount);
        response.setOpenCapasCount(openCapasCount);
        response.setOverdueCount(overdueCapaCount);
        response.setOverdueCapaCount(overdueCapaCount);
        response.setClosedCount(capaRepository.countClosedCapas());
        response.setClosedCapasThisMonth(closedCapasThisMonth);
        response.setDueSoonCount(capaRepository.countDueSoonCapas(today, today.plusDays(7)));
        response.setCapaEscalatedCount(capaRepository.countEscalatedCapas(today.minusDays(7)));
        response.setActionItemOverdueCount(overdueActionItemCount);
        response.setActionItemEscalatedCount(actionItemRepository.countEscalatedActionItems(today.minusDays(7)));
        response.setAverageDaysToClosure(calculateAverageDaysToClosure(capaRepository.findByClosedAtIsNotNull()));
        response.setEffectivenessPassRate(calculateEffectivenessPassRate(
                capaRepository.countEffectivenessPassed(),
                capaRepository.countEffectivenessEvaluated()
        ));
        response.setCapasBySource(mapBreakdown(capaRepository.countCapasBySourceType()));
        response.setCapasByRootCauseCategory(mapBreakdown(capaRepository.countCapasByRootCauseCategory()));
        return response;
    }

    private double calculateAverageDaysToClosure(List<Capa> closedCapas) {
        double average = closedCapas.stream()
                .filter(capa -> capa.getCreatedAt() != null && capa.getClosedAt() != null)
                .mapToLong(capa -> ChronoUnit.DAYS.between(capa.getCreatedAt().toLocalDate(), capa.getClosedAt().toLocalDate()))
                .average()
                .orElse(0);
        return Math.round(average * 10.0) / 10.0;
    }

    private double calculateEffectivenessPassRate(long passedCount, long evaluatedCount) {
        if (evaluatedCount == 0) {
            return 0;
        }
        double passRate = (passedCount * 100.0) / evaluatedCount;
        return Math.round(passRate * 10.0) / 10.0;
    }

    private List<DashboardBreakdownItemResponse> mapBreakdown(List<DashboardCountProjection> projections) {
        return projections.stream()
                .map(projection -> new DashboardBreakdownItemResponse(projection.getLabel(), projection.getCount()))
                .toList();
    }
}
