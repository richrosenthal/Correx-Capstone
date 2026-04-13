import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import {CapaService} from '../../../core/services/capa';
import { RouterLink} from '@angular/router';
import { Capa} from '../../../core/models/capa';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-capa-list',
  imports: [
    NgForOf,
    NgIf,
    RouterLink
  ],
  templateUrl: './capa-list.html',
  styleUrl: './capa-list.css',
})
export class CapaList implements OnInit {
  private capaService = inject(CapaService);
  private cdr = inject(ChangeDetectorRef);

  capas: Capa[] = [];
  isLoading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadCapas();
  }

  loadCapas(): void {
    // this.capaService.getCapas().subscribe(data => {
    //   this.capas = data;
    // });
    this.isLoading = true;

    this.capaService.getCapas().subscribe({
      next: (data) => {
        console.log('CAPAs loaded ok', data);
        this.capas = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('CAPA load failed', err);
        this.errorMessage = 'Failed to load CAPAs.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  searchCapas(search: string): void {
    if (!search.trim()){
      this.loadCapas();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.capaService.searchCapas(search).subscribe({
      next: (data) => {
        this.capas = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Search failed';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

  }
}
