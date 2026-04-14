import { Injectable, inject } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { ActionItem } from '../models/action-item';

export interface ActionItemWritePayload {
  capaId: string;
  title: string;
  description?: string | null;
  statusId: string;
  ownerId?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  evidenceNotes?: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ActionItemService {
  private http = inject(HttpClient);
  private baseUrl = '/api/action-items';

  getByCapaId(capaId: string){
    return this.http.get<ActionItem[]>(`${this.baseUrl}/capa/${capaId}`);
  }
  createActionItem(payload: ActionItemWritePayload){
    return this.http.post<ActionItem>(this.baseUrl, payload);
  }

  updateActionItem(id: string, payload: ActionItemWritePayload) {
    return this.http.put<ActionItem>(`${this.baseUrl}/${id}`, payload);
  }

}
