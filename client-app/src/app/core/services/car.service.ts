import { Injectable } from '@angular/core';
import { ApiService } from '../utilities/api.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../models/car';
import { Reservation } from '../models/reservation';
import { CarsPage } from '../models/cars-page';
import { DatePipe } from '@angular/common';
import { CarFilter } from '../models/car-filter';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  carFilter: CarFilter;
  url = 'cars';
constructor(private apiService: ApiService, private httpClient: HttpClient, private datePipe: DatePipe) {
  this.carFilter = {};
 }
  getAll(startIndex: number, pageSize: number, selectedCategories?: number[], startDate?: Date, endDate?: Date, brand?: string): Observable<CarsPage>{
    this.carFilter = {};
    this.carFilter.startIndex = startIndex;
    this.carFilter.pageSize = pageSize;
    if (selectedCategories.length > 0) {
      this.carFilter.selectedCategoryIds = selectedCategories;
    }
    if (startDate){
      this.carFilter.startDate = this.datePipe.transform(startDate, 'yyyy-MM-dd HH:mm:ss');
    }
    if (endDate){
      this.carFilter.endDate = this.datePipe.transform(endDate, 'yyyy-MM-dd HH:mm:ss');
    }
    if (brand){
      this.carFilter.brand = brand;
    }
    return this.apiService.getWithBody(this.url, this.carFilter);
  }

  getById(id: number): Observable<Car>{
    return this.apiService.get(this.url + '/' + id);
  }

  add(formData: FormData): Observable<any> {
    return this.httpClient.post('http://localhost:8080/cars/add', formData);
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
