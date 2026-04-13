import {inject, Injectable} from '@angular/core';
import {CapaReport} from '../models/capa-report';
import { HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface OpenCapaAgingReportResponse {
  title: string;
  generatedAt: string;
  rows: CapaReport[];
}

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private http = inject(HttpClient);
  private baseUrl = '/api/reports/open-capa-aging';

  getOpenCapaAgingReport(): Observable<OpenCapaAgingReportResponse> {
    return this.http.get<OpenCapaAgingReportResponse>(this.baseUrl);
  }

}
