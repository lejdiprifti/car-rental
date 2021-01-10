import { Injectable } from "@angular/core";
import { Reservation } from "../models/reservation";
import { Observable } from "rxjs/internal/Observable";
import { ApiService } from "../utilities/api.service";
import { ReservationPage } from "../models/reservation-page";
import { DatePipe } from "@angular/common";
import { HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class ReservationService {
  private url: string = "reservations";
  constructor(private apiService: ApiService, private datePipe: DatePipe) {}

  getAll(): Observable<Array<Reservation>> {
    return this.apiService.get(this.url);
  }

  add(model: Reservation): Observable<void> {
    return this.apiService.post(this.url, model);
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
    let params = new HttpParams();
    if (carName) {
      params = params.set("carName", carName);
    }

    if (startDate) {
      params = params.set(
        "startDate",
        this.datePipe.transform(startDate, "yyyy-MM-dd HH:mm:ss")
      );
    }

    if (endDate) {
      params = params.set(
        "endDate",
        this.datePipe.transform(endDate, "yyyy-MM-dd HH:mm:ss")
      );
    }
    return this.apiService.get(
      this.url +
        "/user" +
        "?startIndex=" +
        startIndex +
        "&pageSize=" +
        pageSize,
      params
    );
  }
}
