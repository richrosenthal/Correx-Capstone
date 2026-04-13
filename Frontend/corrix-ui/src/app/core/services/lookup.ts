import { Injectable, inject } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Severity} from '../models/severity';
import { UserLookup } from '../models/user-lookup';
import { SourceType } from '../models/source-type';
import { Status } from '../models/status';


@Injectable({
  providedIn: 'root',
})
export class LookupService {
  private http = inject(HttpClient);
  private baseUrlSeverity = '/api/severities';
  private baseUrlUsers = '/api/users';
  private baseUrlSourceType = '/api/source-types';
  private baseUrlStatus = '/api/statuses';


  getStatuses(){
    return this.http.get<Status[]>(this.baseUrlStatus)
  }

  getSeverities(){
    return this.http.get<Severity[]>(this.baseUrlSeverity)
  }

  getSourceTypes(){
    return this.http.get<SourceType[]>(this.baseUrlSourceType)
  }

  getUsers(){
    return this.http.get<UserLookup[]>(this.baseUrlUsers)
  }

}
