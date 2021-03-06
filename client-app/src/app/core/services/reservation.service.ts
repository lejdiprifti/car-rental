import { Injectable } from "@angular/core";
import { Reservation } from "../models/reservation";
import { Observable } from "rxjs/internal/Observable";
import { ApiService } from "../utilities/api.service";
import { ReservationPage } from "../models/reservation-page";
import { DatePipe } from "@angular/common";
import { HttpParams } from "@angular/common/http";
import { ReservationFilter } from '../models/reservation-filter';

@Injectable({
  providedIn: "root"
})
export class ReservationService {
  private url: string = "reservations";
  private reservationFilter: ReservationFilter;
  constructor(private apiService: ApiService, private datePipe: DatePipe) {}

  getAll(): Observable<Array<Reservation>> {
    return this.apiService.get(this.url);
  }

  add(model: Reservation): Observable<void> {
    return this.apiService.post(this.url + '/add', model);
  }

  edit(model: Reservation, id: number): Observable<void> {
    return this.apiService.put(this.url + "/" + id, model);
  }

  cancel(id: number): Observable<void> {
    return this.apiService.delete(this.url + "/" + id);
  }

  getReservationsByUsername(
    startIndex: number,
    pageSize: number,
    carName?: string,
    startDate?: Date,
    endDate?: Date
  ): Observable<ReservationPage> {
    this.reservationFilter = {};
    this.reservationFilter.startIndex = startIndex;
    this.reservationFilter.pageSize = pageSize;
    if (carName) {
      this.reservationFilter.carName = carName;
    }

    if (startDate) {
      this.reservationFilter.startDate = this.datePipe.transform(startDate, "yyyy-MM-dd HH:mm:ss");
    }

    if (endDate) {
      this.reservationFilter.endDate = this.datePipe.transform(endDate, "yyyy-MM-dd HH:mm:ss");
    }
    return this.apiService.getWithBody(
      this.url +
        "/user", this.reservationFilter
    );
  }
}
