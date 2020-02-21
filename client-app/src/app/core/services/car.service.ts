import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../models/car';
import { Reservation } from '../models/reservation';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  url = 'cars';
constructor(private apiService: ApiService, private httpClient: HttpClient) { }

  getAll(): Observable<Array<Car>>{
    return this.apiService.get(this.url);
  }

  getById(id: number): Observable<Car>{
    return this.apiService.get(this.url + '/' + id);
  }

  add(formData: FormData): Observable<any> {
    return this.httpClient.post('http://localhost:8080/cars', formData);
  }

  edit(formData: FormData, id: number): Observable<any>{
    return this.httpClient.put('http://localhost:8080/cars/'+id, formData);
  }

  delete(id: number): Observable<void>{
    return this.apiService.delete(this.url + '/' + id);
  }

  getReservationsByCar(carId: number): Observable<Array<Reservation>>{
    return this.apiService.get(this.url + '/' + carId+'/reservations');
  }

  cancelByCarAndDate(date: Date, carId: number): Observable<number>{
    return this.apiService.put(this.url + '/' + carId + '/reservations', date);
  }
}
