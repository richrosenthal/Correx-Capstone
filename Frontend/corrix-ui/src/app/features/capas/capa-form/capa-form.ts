import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { CapaService } from '../../../core/services/capa';
import { LookupService } from '../../../core/services/lookup';

import { Status } from '../../../core/models/status';
import { Severity } from '../../../core/models/severity';
import { SourceType } from '../../../core/models/source-type';
import { UserLookup } from '../../../core/models/user-lookup';
import { CapaRequest } from '../../../core/models/capa-request';
import { Capa } from '../../../core/models/capa';
import { CAPA_STAGE_LABELS, CapaStage, isStageAtLeast } from '../../../core/models/capa-stage';

@Component({
  selector: 'app-capa-form',
  imports: [ReactiveFormsModule, NgFor, NgIf, RouterLink],
  templateUrl: './capa-form.html',
  styleUrl: './capa-form.css',
})
export class CapaForm implements OnInit {
  private fb = inject(FormBuilder);
  private capaService = inject(CapaService);
  private lookupService = inject(LookupService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  readonly stageLabels = CAPA_STAGE_LABELS;

  isEditMode = false;
  capaId: string | null = null;
  pageTitle = 'New CAPA';
  currentStage: CapaStage = 'DRAFT';

  statuses: Status[] = [];
  severities: Severity[] = [];
  sourceTypes: SourceType[] = [];
  users: UserLookup[] = [];
  loadedCapa: Capa | null = null;

  errorMessage = '';
  submitAttempted = false;

  capaForm = this.fb.group({
    capaNumber: ['', Validators.required],
    title: ['', Validators.required],
    description: [''],
    statusId: ['', Validators.required],
    severityId: ['', Validators.required],
    sourceTypeId: ['', Validators.required],
    ownerId: ['', Validators.required],
    dueDate: [''],
    rootCauseCategory: [''],
    effectivenessPassed: [null as boolean | null],
  });

  ngOnInit(): void {
    this.loadLookups();

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');

      if (id) {
        this.isEditMode = true;
        this.capaId = id;
        this.pageTitle = 'Edit CAPA';

        this.loadCapa(id);
      }
    });
  }

  get canShowInvestigationFields(): boolean {
    return isStageAtLeast(this.currentStage, 'INVESTIGATION');
  }

  get canShowEffectivenessFields(): boolean {
    return isStageAtLeast(this.currentStage, 'EFFECTIVENESS_CHECK');
  }

  loadCapa(id: string): void {
    this.capaService.getCapaById(id).subscribe({
      next: (data) => {
        this.loadedCapa = data;
        this.currentStage = data.stage ?? 'DRAFT';
        this.patchFormFromCapa(data);
      },
      error: (err) => {
        this.errorMessage = this.readApiError(err, 'Failed to load CAPA.');
      },
    });
  }

  loadLookups(): void {
    this.lookupService.getStatuses().subscribe((data) => {
      this.statuses = data;
      this.patchLoadedCapaIfReady();
    });
    this.lookupService.getSeverities().subscribe((data) => {
      this.severities = data;
      this.patchLoadedCapaIfReady();
    });
    this.lookupService.getSourceTypes().subscribe((data) => {
      this.sourceTypes = data;
      this.patchLoadedCapaIfReady();
    });
    this.lookupService.getUsers().subscribe((data) => {
      this.users = data;
      this.patchLoadedCapaIfReady();
    });
  }

  onSubmit(): void {
    this.submitAttempted = true;

    if (this.capaForm.invalid) {
      this.capaForm.markAllAsTouched();
      return;
    }

    const payload: CapaRequest = {
      capaNumber: this.capaForm.value.capaNumber ?? '',
      title: this.capaForm.value.title ?? '',
      description: this.capaForm.value.description ?? '',
      statusId: this.capaForm.value.statusId ?? '',
      severityId: this.capaForm.value.severityId ?? '',
      sourceTypeId: this.capaForm.value.sourceTypeId ?? '',
      ownerId: this.capaForm.value.ownerId ?? '',
      dueDate: this.capaForm.value.dueDate ?? '',
      rootCauseCategory: this.capaForm.value.rootCauseCategory ?? null,
      effectivenessPassed: this.capaForm.value.effectivenessPassed ?? null,
    };

    if (this.isEditMode && this.capaId) {
      this.capaService.updateCapa(this.capaId, payload).subscribe({
        next: () => {
          this.router.navigate(['/capas', this.capaId]);
        },
        error: (err) => {
          this.errorMessage = this.readApiError(err, 'Failed to update CAPA.');
        },
      });
    } else {
      this.capaService.createCapa(payload).subscribe({
        next: (createdCapa) => {
          this.router.navigate(['/capas', createdCapa.id]);
        },
        error: (err) => {
          this.errorMessage = this.readApiError(err, 'Failed to create CAPA.');
        },
      });
    }
  }

  private patchLoadedCapaIfReady(): void {
    if (this.loadedCapa) {
      this.patchFormFromCapa(this.loadedCapa);
    }
  }

  private patchFormFromCapa(data: Capa): void {
    const matchedStatus = this.statuses.find((status) => status.name === data.status);
    const matchedSeverity = this.severities.find((severity) => severity.name === data.severity);
    const matchedSourceType = this.sourceTypes.find((sourceType) => sourceType.name === data.sourceType);
    const matchedOwner = this.users.find((user) => user.username === data.owner);

    this.capaForm.patchValue({
      capaNumber: data.capaNumber ?? '',
      title: data.title ?? '',
      description: data.description ?? '',
      statusId: matchedStatus?.id ?? '',
      severityId: matchedSeverity?.id ?? '',
      sourceTypeId: matchedSourceType?.id ?? '',
      ownerId: matchedOwner?.id ?? '',
      dueDate: data.dueDate ?? '',
      rootCauseCategory: data.rootCauseCategory ?? '',
      effectivenessPassed: data.effectivenessPassed ?? null,
    });
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
