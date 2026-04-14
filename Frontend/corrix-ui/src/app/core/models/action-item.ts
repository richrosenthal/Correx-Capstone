export interface ActionItem {
  id: string;
  capaId: string;
  title: string;
  description?: string | null;
  status: string;
  owner?: string | null;
  assignee?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  evidenceNotes?: string | null;
  overdue: boolean;
  escalationStatus: string;
  createdAt: string;
  updatedAt: string;
}
