export interface DashboardBreakdownItem {
  label: string;
  count: number;
}

export interface DashboardSummary {
  openCount: number;
  overdueCount: number;
  dueSoonCount: number;
  closedCount: number;
  capaEscalatedCount: number;
  actionItemOverdueCount: number;
  actionItemEscalatedCount: number;
  openCapasCount: number;
  closedCapasThisMonth: number;
  overdueCapaCount: number;
  averageDaysToClosure: number;
  effectivenessPassRate: number;
  capasBySource: DashboardBreakdownItem[];
  capasByRootCauseCategory: DashboardBreakdownItem[];
}
