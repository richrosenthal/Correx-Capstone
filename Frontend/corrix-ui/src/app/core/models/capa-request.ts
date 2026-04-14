import { CapaStage } from './capa-stage';

export interface CapaRequest {
  capaNumber: string;
  title: string;
  description?: string | null;
  stage?: CapaStage | null;
  rootCauseCategory?: string | null;
  effectivenessPassed?: boolean | null;
  statusId: string;
  severityId: string;
  sourceTypeId: string;
  ownerId?: string | null;
  dueDate?: string | null;
}
