import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { OnInit } from '@angular/core';
import {ReportService} from '../../../core/services/report';
import { CapaReport} from '../../../core/models/capa-report';
import {ActivatedRoute} from '@angular/router';
import {NgForOf, NgIf, DatePipe} from '@angular/common';

@Component({
  selector: 'app-aging-report',
  imports: [
    NgForOf,
    NgIf,
    DatePipe
  ],
  templateUrl: './aging-report.html',
  styleUrl: './aging-report.css',
})
export class AgingReport implements OnInit{
  private cdr = inject(ChangeDetectorRef);
  private reportService = inject(ReportService);
  private route = inject(ActivatedRoute);
  errorMessage = '';
  isLoading = true;
  rows: CapaReport[] = [];
  reportTitle = '';
  generatedAt = '';

  ngOnInit(): void {
    this.loadReport();
  }
  loadReport(): void {
    this.isLoading = true;

    this.reportService.getOpenCapaAgingReport().subscribe({
      next: (data) => {
        this.reportTitle = data.title;
        this.generatedAt = data.generatedAt;
        this.rows = data.rows;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load CAPA.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

}
