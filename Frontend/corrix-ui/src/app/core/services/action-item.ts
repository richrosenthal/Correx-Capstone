import { Injectable, inject } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { ActionItem } from '../models/action-item';


@Injectable({
  providedIn: 'root',
})
export class ActionItemService {
  private http = inject(HttpClient);
  private baseUrl = '/api/action-items';

  getByCapaId(capaId: string){
    return this.http.get<ActionItem[]>(`${this.baseUrl}/capa/${capaId}`);
  }
  createActionItem(payload: {
    capaId: string;
    title: string;
    description?: string | null;
    statusId: string;
    ownerId?: string | null;
    dueDate?: string | null;
    completedDate?: string | null;
    evidenceNotes?: string | null;
  }){
    return this.http.post<ActionItem>(this.baseUrl, payload);
  }

}
