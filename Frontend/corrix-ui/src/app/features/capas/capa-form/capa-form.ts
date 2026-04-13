import { Component, OnInit, inject } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';

import { CapaService } from '../../../core/services/capa';
import { LookupService } from '../../../core/services/lookup';

import {Status} from '../../../core/models/status';
import {Severity} from '../../../core/models/severity';
import {SourceType} from '../../../core/models/source-type';
import {UserLookup} from '../../../core/models/user-lookup';
import {CapaRequest} from '../../../core/models/capa-request';

@Component({
  selector: 'app-capa-form',
  imports: [ReactiveFormsModule, NgFor, NgIf, RouterLink, FormsModule],
  templateUrl: './capa-form.html',
  styleUrl: './capa-form.css',
})
export class CapaForm implements OnInit {
  private fb = inject(FormBuilder);
  private capaService = inject(CapaService);
  private lookupService = inject(LookupService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  isEditMode = false;
  capaId: string | null = null;
  pageTitle = 'New CAPA';

  statuses: Status[] = [];
  severities: Severity[] = [];
  sourceTypes: SourceType[] = [];
  users: UserLookup[] = [];

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
    dueDate: ['']
  });

  ngOnInit(): void {
    this.loadLookups();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');

      if (id) {
        this.isEditMode = true;
        this.capaId = id;
        this.pageTitle = 'Edit CAPA';

        this.loadCapa(id);
      }
    });
  }

  loadCapa(id: string): void {
    this.capaService.getCapaById(id).subscribe({
      next: (data) => {
        const matchedStatus = this.statuses.find(s => s.name === data.status)
        const matchedSeverity = this.severities.find(s => s.name === data.severity)
        const matchedSourceType = this.sourceTypes.find(s => s.name === data.sourceType)
        const matchedOwner = this.users.find(u => u.username === data.owner)

        this.capaForm.patchValue({
          capaNumber: data.capaNumber ?? '',
          title: data.title ?? '',
          description: data.description ?? '',
          statusId: matchedStatus?.id ?? '',
          severityId: matchedSeverity?.id ?? '',
          sourceTypeId: matchedSourceType?.id ?? '',
          ownerId: matchedOwner?.id ?? '',
          dueDate: data.dueDate ?? ''
        });
      },
      error: () => {
        this.errorMessage = 'Failed to load';
      }
    })
  }

  loadLookups(): void {
    this.lookupService.getStatuses().subscribe(data => {
      this.statuses = data;
    });
    this.lookupService.getSeverities().subscribe(data => {
      this.severities = data;
    });
    this.lookupService.getSourceTypes().subscribe(data => {
      this.sourceTypes = data;
    });
    this.lookupService.getUsers().subscribe(data => {
      this.users = data;
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
      dueDate: this.capaForm.value.dueDate ?? ''
    };

    if (this.isEditMode && this.capaId){
      this.capaService.updateCapa(this.capaId, payload).subscribe({
        next: () => {
          this.router.navigate(['/capas']);
        },
        error: () => {
          this.errorMessage = 'Failed to update CAPA';
        }
      })
    } else {
      this.capaService.createCapa(payload).subscribe({
        next: () => {
          this.router.navigate(['/capas']);
        },
        error: () => {
          this.errorMessage = 'Failed to create CAPA';
        }
      });
    }

  }


}
