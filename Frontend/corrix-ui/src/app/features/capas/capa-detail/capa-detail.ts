import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgForOf, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CapaService } from '../../../core/services/capa';
import { ActionItemService, ActionItemWritePayload } from '../../../core/services/action-item';
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
  actionItemErrorMessage = '';
  actionItems: ActionItem[] = [];
  statuses: Status[] = [];
  users: UserLookup[] = [];
  editingActionItemId: string | null = null;
  isSavingActionItem = false;

  actionItemForm = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    statusId: ['', Validators.required],
    ownerId: ['', Validators.required],
    dueDate: ['', Validators.required],
    completedDate: [''],
    evidenceNotes: [''],
  });

  editActionItemForm = this.fb.group({
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
        if (this.editingActionItemId && !data.some((item) => item.id === this.editingActionItemId)) {
          this.cancelEditActionItem();
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.actionItemErrorMessage = 'Failed to load action items.';
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

    this.actionItemErrorMessage = '';
    this.isSavingActionItem = true;
    const payload = this.buildActionItemPayload(this.actionItemForm.getRawValue(), this.capa.id);

    this.actionItemService.createActionItem(payload).subscribe({
      next: () => {
        this.actionItemForm.reset();
        this.isSavingActionItem = false;
        this.loadActionItems(this.capa!.id);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.actionItemErrorMessage = this.readApiError(err, 'Failed to create action item.');
        this.isSavingActionItem = false;
      },
    });
  }

  startEditActionItem(item: ActionItem): void {
    this.editingActionItemId = item.id;
    this.actionItemErrorMessage = '';

    const matchedStatus = this.statuses.find((status) => status.name === item.status);
    const ownerName = item.owner || item.assignee;
    const matchedOwner = this.users.find((user) => user.username === ownerName);

    this.editActionItemForm.patchValue({
      title: item.title ?? '',
      description: item.description ?? '',
      statusId: matchedStatus?.id ?? '',
      ownerId: matchedOwner?.id ?? '',
      dueDate: item.dueDate ?? '',
      completedDate: item.completedDate ?? '',
      evidenceNotes: item.evidenceNotes ?? '',
    });
  }

  cancelEditActionItem(): void {
    this.editingActionItemId = null;
    this.editActionItemForm.reset();
    this.actionItemErrorMessage = '';
  }

  onUpdateActionItem(): void {
    if (!this.capa?.id || !this.editingActionItemId) {
      return;
    }

    if (this.editActionItemForm.invalid) {
      this.editActionItemForm.markAllAsTouched();
      return;
    }

    this.actionItemErrorMessage = '';
    this.isSavingActionItem = true;
    const payload = this.buildActionItemPayload(this.editActionItemForm.getRawValue(), this.capa.id);

    this.actionItemService.updateActionItem(this.editingActionItemId, payload).subscribe({
      next: () => {
        this.isSavingActionItem = false;
        this.cancelEditActionItem();
        this.loadActionItems(this.capa!.id);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.actionItemErrorMessage = this.readApiError(err, 'Failed to update action item.');
        this.isSavingActionItem = false;
      },
    });
  }

  isActionItemClosed(item: ActionItem): boolean {
    return ['closed', 'done', 'completed'].includes(item.status.toLowerCase());
  }

  private stageIndex(stage: CapaStage): number {
    return this.stages.indexOf(stage);
  }

  private buildActionItemPayload(
    formValue: {
      title?: string | null;
      description?: string | null;
      statusId?: string | null;
      ownerId?: string | null;
      dueDate?: string | null;
      completedDate?: string | null;
      evidenceNotes?: string | null;
    },
    capaId: string,
  ): ActionItemWritePayload {
    return {
      capaId,
      title: formValue.title ?? '',
      description: formValue.description ?? '',
      statusId: formValue.statusId ?? '',
      ownerId: formValue.ownerId || null,
      dueDate: formValue.dueDate || null,
      completedDate: formValue.completedDate || null,
      evidenceNotes: formValue.evidenceNotes || null,
    };
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
