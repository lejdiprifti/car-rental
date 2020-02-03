import { Injectable } from '@angular/core';
import { Reservation } from '../models/reservation';
import { Observable } from 'rxjs/internal/Observable';
import { ApiService } from '../utilities/api.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
private url : string = 'reservations'
constructor(private apiService: ApiService) { }

getAll(): Observable<Array<Reservation>>{
  return this.apiService.get(this.url);
}

add(model: Reservation): Observable<void> {
  return this.apiService.post(this.url, model);
}

edit(model: Reservation, id: number): Observable<void>{
  return this.apiService.put(this.url+'/'+id, model);
}

cancel(id: number): Observable<void>{
  return this.apiService.delete(this.url+'/'+id);
}
}
