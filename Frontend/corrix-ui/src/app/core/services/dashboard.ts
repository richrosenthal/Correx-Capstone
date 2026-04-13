import { Injectable, inject } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import {DashboardSummary} from '../models/dashboard-summary';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private http = inject(HttpClient);
  private baseUrl = '/api/dashboard';

  getSummary() {
    return this.http.get<DashboardSummary>(`${this.baseUrl}/summary`);
  }

}
