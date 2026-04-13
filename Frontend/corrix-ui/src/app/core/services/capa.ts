import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams} from '@angular/common/http';
import { Capa } from '../models/capa';
import {CapaRequest} from '../models/capa-request';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class CapaService {
  private http = inject(HttpClient);
  private baseUrl = '/api/capas';

  getCapas() {
    return this.http.get<Capa[]>(this.baseUrl)
  }

  getCapaById(id: string){
    return this.http.get<Capa>(`${this.baseUrl}/${id}`);
  }

  updateCapa(id: string, payload: CapaRequest) {
    return this.http.put<Capa>(`${this.baseUrl}/${id}`, payload);
  }

  searchCapas(search: string) {
    const params = new HttpParams().set('q', search);
    return this.http.get<Capa[]>(this.baseUrl, { params });
  }

  createCapa(payload: CapaRequest): Observable<Capa>{
    return this.http.post<Capa>(this.baseUrl, payload);
  }

  deleteCapa(id: string){
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
