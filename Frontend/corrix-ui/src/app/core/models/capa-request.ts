export interface CapaRequest {
  capaNumber: string;
  title: string;
  description?: string | null;
  statusId: string;
  severityId: string;
  sourceTypeId: string;
  ownerId?: string | null;
  dueDate?: string | null;
}
