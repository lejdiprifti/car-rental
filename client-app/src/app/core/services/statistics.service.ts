import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { Statistics } from '../models/statistics';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  url = "statistics"
constructor(private apiService: ApiService) { }

  getStatistics(): Observable<Statistics> {
    return this.apiService.get(this.url);
  }
}
