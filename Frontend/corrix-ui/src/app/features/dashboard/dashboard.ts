import {Component, ChangeDetectorRef, inject} from '@angular/core';
import {DashboardService} from '../../core/services/dashboard';
import {DashboardSummary} from '../../core/models/dashboard-summary';
import {OnInit} from '@angular/core';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [
    NgIf
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit{
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



}
