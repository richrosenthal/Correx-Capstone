import {CapaReport} from './capa-report';

export interface OpenCapaAgingReportResponse {
  title: string;
  generatedAt: string;
  rows: CapaReport[];
}
