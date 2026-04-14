import { CapaStage } from './capa-stage';

export interface Capa {
  id: string;
  capaNumber: string;
  title: string;
  description: string;
  stage: CapaStage;
  status: string;
  severity: string;
  sourceType: string;
  owner: string;
  rootCauseCategory?: string | null;
  effectivenessPassed?: boolean | null;
  dueDate: string;
  overdue: boolean;
  escalationStatus: string;
  closedAt?: string | null;
  createdAt: string;
  updatedAt: string;
}
