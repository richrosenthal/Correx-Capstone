import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';

import { DashboardService } from '../../core/services/dashboard';
import { DashboardBreakdownItem, DashboardSummary } from '../../core/models/dashboard-summary';

@Component({
  selector: 'app-dashboard',
  imports: [NgIf, NgForOf],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  summary: DashboardSummary | null = null;
  errorMessage = '';

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getSummary().subscribe({
      next: (data) => {
        this.summary = data;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load dashboard summary.';
        this.cdr.detectChanges();
      }
    });
  }

  get sourceBreakdown(): DashboardBreakdownItem[] {
    return this.summary?.capasBySource ?? [];
  }

  get rootCauseBreakdown(): DashboardBreakdownItem[] {
    return this.summary?.capasByRootCauseCategory ?? [];
  }

  get maxSourceCount(): number {
    return this.maxCount(this.sourceBreakdown);
  }

  get maxRootCauseCount(): number {
    return this.maxCount(this.rootCauseBreakdown);
  }

  formatMetric(value: number | null | undefined, suffix = ''): string {
    if (value == null || Number.isNaN(value)) {
      return '--';
    }

    const displayValue = Number.isInteger(value) ? `${value}` : value.toFixed(1);
    return `${displayValue}${suffix}`;
  }

  barWidth(itemCount: number, maxCount: number): number {
    if (itemCount <= 0 || maxCount <= 0) {
      return 0;
    }

    return Math.max((itemCount / maxCount) * 100, 8);
  }

  trackByLabel(_: number, item: DashboardBreakdownItem): string {
    return item.label;
  }

  private maxCount(items: DashboardBreakdownItem[]): number {
    return items.reduce((max, item) => Math.max(max, item.count), 0);
  }
}
