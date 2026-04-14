package com.rrosenthal.corrix.dto;

public class DashboardSummaryResponse {

    private long openCount;
    private long overdueCount;
    private long dueSoonCount;
    private long closedCount;
    private long capaEscalatedCount;
    private long actionItemOverdueCount;
    private long actionItemEscalatedCount;

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
}
