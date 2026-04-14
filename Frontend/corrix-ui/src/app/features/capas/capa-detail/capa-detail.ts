import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgForOf, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CapaService } from '../../../core/services/capa';
import { ActionItemService } from '../../../core/services/action-item';
import { LookupService } from '../../../core/services/lookup';

import { Capa } from '../../../core/models/capa';
import { ActionItem } from '../../../core/models/action-item';
import {
  CAPA_STAGE_LABELS,
  CAPA_STAGES,
  CapaStage,
  getNextCapaStage,
  isStageAtLeast,
} from '../../../core/models/capa-stage';
import { UserLookup } from '../../../core/models/user-lookup';
import { Status } from '../../../core/models/status';

@Component({
  selector: 'app-capa-detail',
  imports: [RouterLink, NgForOf, NgIf, ReactiveFormsModule],
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

  readonly stages = CAPA_STAGES;
  readonly stageLabels = CAPA_STAGE_LABELS;

  capa: Capa | null = null;
  isLoading = true;
  isTransitioning = false;
  errorMessage = '';
  actionItems: ActionItem[] = [];
  statuses: Status[] = [];
  users: UserLookup[] = [];

  actionItemForm = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    statusId: ['', Validators.required],
    ownerId: ['', Validators.required],
    dueDate: ['', Validators.required],
    completedDate: [''],
    evidenceNotes: [''],
  });

  ngOnInit(): void {
    this.loadLookups();
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');

      if (id) {
        this.loadCapa(id);
      } else {
        this.errorMessage = 'CAPA ID not found.';
        this.isLoading = false;
      }
    });
  }

  get currentStage(): CapaStage {
    return this.capa?.stage ?? 'DRAFT';
  }

  get nextStage(): CapaStage | null {
    return getNextCapaStage(this.currentStage);
  }

  get canManageActionItems(): boolean {
    return this.capa !== null && isStageAtLeast(this.currentStage, 'ACTION_PLAN');
  }

  get canShowInvestigation(): boolean {
    return this.capa !== null && isStageAtLeast(this.currentStage, 'INVESTIGATION');
  }

  get canShowEffectiveness(): boolean {
    return this.capa !== null && isStageAtLeast(this.currentStage, 'EFFECTIVENESS_CHECK');
  }

  get stageSummary(): string {
    switch (this.currentStage) {
      case 'DRAFT':
        return 'Capture the issue and prepare the record for triage.';
      case 'TRIAGE':
        return 'Containment and prioritization are in focus for this CAPA.';
      case 'INVESTIGATION':
        return 'Root cause analysis and ownership should be clearly defined.';
      case 'ACTION_PLAN':
        return 'Action items should be planned, owned, and due-dated.';
      case 'IMPLEMENTATION':
        return 'Execution is underway and action item progress matters most.';
      case 'EFFECTIVENESS_CHECK':
        return 'Verify whether the actions resolved the issue effectively.';
      case 'CLOSED':
        return 'This CAPA has completed the workflow.';
    }
  }

  loadLookups(): void {
    this.lookupService.getStatuses().subscribe((data) => {
      this.statuses = data;
    });

    this.lookupService.getUsers().subscribe((data) => {
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
      error: (err) => {
        this.errorMessage = this.readApiError(err, 'Failed to load CAPA.');
        this.isLoading = false;
      },
    });
  }

  loadActionItems(capaId: string): void {
    this.actionItemService.getByCapaId(capaId).subscribe({
      next: (data) => {
        this.actionItems = data;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load action items.';
      },
    });
  }

  isStageComplete(stage: CapaStage): boolean {
    return this.stageIndex(stage) < this.stageIndex(this.currentStage);
  }

  isStageCurrent(stage: CapaStage): boolean {
    return stage === this.currentStage;
  }

  isStageUnlocked(stage: CapaStage): boolean {
    return this.stageIndex(stage) <= this.stageIndex(this.currentStage);
  }

  onAdvanceStage(): void {
    if (!this.capa?.id || !this.nextStage || this.isTransitioning) {
      return;
    }

    this.isTransitioning = true;
    this.errorMessage = '';

    this.capaService.transitionStage(this.capa.id, this.nextStage).subscribe({
      next: (updatedCapa) => {
        this.capa = updatedCapa;
        this.isTransitioning = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = this.readApiError(err, 'Failed to transition CAPA.');
        this.isTransitioning = false;
      },
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
      error: (err) => {
        this.errorMessage = this.readApiError(err, 'Failed to delete CAPA.');
      },
    });
  }

  onAddActionItem(): void {
    if (this.actionItemForm.invalid || !this.capa?.id) {
      this.actionItemForm.markAllAsTouched();
      return;
    }

    const payload = {
      capaId: this.capa.id,
      title: this.actionItemForm.value.title ?? '',
      description: this.actionItemForm.value.description ?? '',
      statusId: this.actionItemForm.value.statusId ?? '',
      ownerId: this.actionItemForm.value.ownerId || null,
      dueDate: this.actionItemForm.value.dueDate || null,
      completedDate: this.actionItemForm.value.completedDate || null,
      evidenceNotes: this.actionItemForm.value.evidenceNotes || null,
    };

    this.actionItemService.createActionItem(payload).subscribe({
      next: () => {
        this.actionItemForm.reset();
        this.loadActionItems(this.capa!.id);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = this.readApiError(err, 'Failed to create action item.');
      },
    });
  }

  private stageIndex(stage: CapaStage): number {
    return this.stages.indexOf(stage);
  }

  private readApiError(err: any, fallback: string): string {
    const error = err?.error;
    if (!error) {
      return fallback;
    }

    if (Array.isArray(error.errors) && error.errors.length > 0) {
      return [error.error, ...error.errors].filter(Boolean).join(' ');
    }

    if (typeof error.error === 'string' && error.error.trim()) {
      return error.error;
    }

    return fallback;
  }
}
