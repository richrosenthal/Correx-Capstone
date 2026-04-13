import { Injectable, inject } from '@angular/core';
import { HttpClient} from '@angular/common/http';



@Injectable({
  providedIn: 'root',
})
export class ActionItemService {
  private http = inject(HttpClient);
  private baseUrl = '/api/action-items';

  getByCapaId(capaId: string){
    return this.http.get<any[]>(`${this.baseUrl}/capa/${capaId}`);
  }
  createActionItem(payload: any){
    return this.http.post<any>(this.baseUrl, payload);
  }

}
