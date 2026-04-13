import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {CapaService} from '../../../core/services/capa';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { Capa} from '../../../core/models/capa';
import {NgForOf, NgIf} from '@angular/common';
import {ActionItemService} from '../../../core/services/action-item';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {UserLookup} from '../../../core/models/user-lookup';
import {Status} from '../../../core/models/status';
import {LookupService} from '../../../core/services/lookup';


@Component({
  selector: 'app-capa-detail',
  imports: [RouterLink, NgForOf,
    NgIf, ReactiveFormsModule],
  templateUrl: './capa-detail.html',
  styleUrl: './capa-detail.css',
})
export class CapaDetail implements OnInit {
  private capaService = inject(CapaService);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);
  private actionItemService = inject(ActionItemService);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private lookupService = inject(LookupService);


  capa: Capa | null = null;
  isLoading = true;
  errorMessage = '';
  actionItems: any[] = [];
  statuses: Status[] = [];
  users: UserLookup[] = [];

  actionItemForm = this.fb.group({
    title: ['', Validators.required],
    statusId: [''],
    assigneeId: [''],
    dueDate: ['']
  });

  ngOnInit(): void {
    this.loadLookups();
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');

      if (id){
        this.loadCapa(id);
      }else {
        this.errorMessage = 'CAPA ID not found.';
        this.isLoading = false;
      }
    });
  }
  loadLookups(): void {
    this.lookupService.getStatuses().subscribe(data => {
      this.statuses = data;
    });

    this.lookupService.getUsers().subscribe(data => {
      this.users = data;
    });
  }

  loadCapa(id: string): void {
    this.capaService.getCapaById(id).subscribe({
      next: (data) => {
        this.capa = data;
        this.loadActionItems(id);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load CAPA.';
        this.isLoading = false;
      }
    });
}
loadActionItems(capaId: string): void {
    this.actionItemService.getByCapaId(capaId).subscribe({
      next: (data) => {
        this.actionItems = data;
        this.cdr.detectChanges();
      },
      error: () => {
        console.error('Failed to load action items');
      }
    });
}
  onDelete(): void {
    if (!this.capa?.id) return;

    const confirmed = confirm('Are you sure you want to delete this CAPA?');

    if (!confirmed) return;

    this.capaService.deleteCapa(this.capa.id).subscribe({
      next: () => {
        this.router.navigate(['/capas']);
      },
      error: () => {
        this.errorMessage = 'Failed to delete CAPA.';
      }
    });
  }

  onAddActionItem(): void {
    console.log('FORM VALUE:', this.actionItemForm.value);
    console.log('FORM VALID:', this.actionItemForm.valid);
    if (this.actionItemForm.invalid || !this.capa?.id){
      this.actionItemForm.markAllAsTouched();
      return;
    }
    const payload = {
      capaId: this.capa.id,
      title: this.actionItemForm.value.title ?? '',
      statusId: this.actionItemForm.value.statusId ?? '',
      assigneeId: this.actionItemForm.value.assigneeId || null,
      dueDate: this.actionItemForm.value.dueDate || null
    };

    this.actionItemService.createActionItem(payload).subscribe({
      next: () => {
        this.actionItemForm.reset();
        this.loadActionItems(this.capa!.id);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failled to create action item', err);
        this.errorMessage = 'Failed to create action item';
      }
    });
  }
}
