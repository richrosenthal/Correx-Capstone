package com.rrosenthal.corrix.dto;

import java.util.List;

public class DashboardSummaryResponse {

    private long openCount;
    private long overdueCount;
    private long dueSoonCount;
    private long closedCount;
    private long capaEscalatedCount;
    private long actionItemOverdueCount;
    private long actionItemEscalatedCount;
    private long openCapasCount;
    private long closedCapasThisMonth;
    private long overdueCapaCount;
    private double averageDaysToClosure;
    private double effectivenessPassRate;
    private List<DashboardBreakdownItemResponse> capasBySource;
    private List<DashboardBreakdownItemResponse> capasByRootCauseCategory;

    public long getOpenCount() {
        return openCount;
    }
    public void setOpenCount(long openCount) {
        this.openCount = openCount;
    }
    public long getOverdueCount() {
        return overdueCount;
    }
    public void setOverdueCount(long overdueCount) {
        this.overdueCount = overdueCount;
    }
    public long getClosedCount() {
        return closedCount;
    }
    public void setClosedCount(long closedCount) {
        this.closedCount = closedCount;
    }
    public long getDueSoonCount() {
        return dueSoonCount;
    }
    public void setDueSoonCount(long dueSoonCount) {
        this.dueSoonCount = dueSoonCount;
    }
    public long getCapaEscalatedCount() {
        return capaEscalatedCount;
    }
    public void setCapaEscalatedCount(long capaEscalatedCount) {
        this.capaEscalatedCount = capaEscalatedCount;
    }
    public long getActionItemOverdueCount() {
        return actionItemOverdueCount;
    }
    public void setActionItemOverdueCount(long actionItemOverdueCount) {
        this.actionItemOverdueCount = actionItemOverdueCount;
    }
    public long getActionItemEscalatedCount() {
        return actionItemEscalatedCount;
    }
    public void setActionItemEscalatedCount(long actionItemEscalatedCount) {
        this.actionItemEscalatedCount = actionItemEscalatedCount;
    }
    public long getOpenCapasCount() {
        return openCapasCount;
    }
    public void setOpenCapasCount(long openCapasCount) {
        this.openCapasCount = openCapasCount;
    }
    public long getClosedCapasThisMonth() {
        return closedCapasThisMonth;
    }
    public void setClosedCapasThisMonth(long closedCapasThisMonth) {
        this.closedCapasThisMonth = closedCapasThisMonth;
    }
    public long getOverdueCapaCount() {
        return overdueCapaCount;
    }
    public void setOverdueCapaCount(long overdueCapaCount) {
        this.overdueCapaCount = overdueCapaCount;
    }
    public double getAverageDaysToClosure() {
        return averageDaysToClosure;
    }
    public void setAverageDaysToClosure(double averageDaysToClosure) {
        this.averageDaysToClosure = averageDaysToClosure;
    }
    public double getEffectivenessPassRate() {
        return effectivenessPassRate;
    }
    public void setEffectivenessPassRate(double effectivenessPassRate) {
        this.effectivenessPassRate = effectivenessPassRate;
    }
    public List<DashboardBreakdownItemResponse> getCapasBySource() {
        return capasBySource;
    }
    public void setCapasBySource(List<DashboardBreakdownItemResponse> capasBySource) {
        this.capasBySource = capasBySource;
    }
    public List<DashboardBreakdownItemResponse> getCapasByRootCauseCategory() {
        return capasByRootCauseCategory;
    }
    public void setCapasByRootCauseCategory(List<DashboardBreakdownItemResponse> capasByRootCauseCategory) {
        this.capasByRootCauseCategory = capasByRootCauseCategory;
    }
}
