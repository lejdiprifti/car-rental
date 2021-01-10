import { Component, OnInit } from '@angular/core';

import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ReservationService } from '@ikubinfo/core/services/reservation.service';
import { Reservation } from '@ikubinfo/core/models/reservation';
import { cols } from '@ikubinfo/suggestion/bookings/bookings.constants';

import { ConfirmationService } from 'primeng/primeng';

@Component({
  selector: 'ikubinfo-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.css']
})
export class BookingsComponent implements OnInit {
  reservations: Array<Reservation>;
  selectedReservation: Reservation;
  displayDialog: boolean;
  reservation: Reservation = {};
  cols: any[];
  newReservation: boolean;
  startTime: Date;
  startDate: Date;
  endTime: Date;
  endDate: Date;
  reservedDates: Array<Date>;
  minDate: Date;
  fee: number = 0;
  price: number;
  first: number;
  totalRecords: number;
  carName: string;
  startFilterDate: Date;
  endFilterDate: Date;
  advancedFilters: boolean = false;
  constructor(private confirmationService: ConfirmationService,
    private reservationService: ReservationService, private logger: LoggerService) {}

  ngOnInit() {
    this.reservation = {};
    this.cols = cols;
    this.first = 0;
    this.getMyReservations(0,5, this.carName, this.startDate, this.endDate);
    this.reservedDates = [];
    this.minDate = new Date();
  }


  getMyReservations(startIndex: number, pageSize: number, carName?: string, startDate?: Date, endDate?: Date): void {
    this.reservationService.getReservationsByUsername(startIndex, pageSize, carName,startDate,endDate).subscribe(res => {
      this.reservations = res.reservationList;
      this.totalRecords = res.totalRecords;
      this.reservations.forEach(el => {
        this.startDate = new Date(el.startDate);
        this.startTime = new Date(el.startDate);
        this.endDate = new Date(el.endDate);
        this.endTime = new Date(el.endDate);
        this.reservation = {};
        this.price = el.car.price;
        this.calculateFee();
        el.fee = Number(this.fee.toFixed(2));
      })
      this.defineReservedDates();
    }, err => {
      this.logger.error('Error', err.error.message);
    })
  }
  onRowSelect(event) {
    let startDate = new Date(event.data.startDate);
    let endDate = new Date(event.data.endDate);
    let today = new Date();
    if (startDate.getTime() <= today.getTime() && endDate.getTime() >= today.getTime()) {
      this.logger.warning('Warning!', 'You cannot edit active reservations.');
    } else if (endDate.getTime() < today.getTime()) {
      this.logger.warning('Warning!', 'You cannot edit previous reservations.');
    } else {
      this.newReservation = false;
      this.reservation = this.cloneCategory(event.data);
      this.displayDialog = true;
    }
  }

  cloneCategory(r: Reservation): Reservation {
    let reservation: Reservation = {};
    this.startDate = new Date(r.startDate);
    this.endDate = new Date(r.endDate);
    this.startTime = this.startDate;
    this.endTime = this.endDate;
    this.price = r.car.price;
    this.calculateFee();
    return reservation;
  }

  save(): void {
    let reservations = [...this.reservations];
    reservations[this.reservations.indexOf(this.selectedReservation)] = this.reservation;
    this.confirmationService.confirm({
      message: 'Are you sure you want to save this category?',
      header: 'Accept Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        let reservation: Reservation = {};
        reservation.startDate = new Date(Date.UTC(this.startDate.getFullYear(), this.startDate.getMonth(), this.startDate.getDate(), this.startTime.getHours(), this.startTime.getMinutes()));
        reservation.endDate = new Date(Date.UTC(this.endDate.getFullYear(), this.endDate.getMonth(), this.endDate.getDate(), this.endTime.getHours(), this.endTime.getMinutes()));
        reservation.fee = this.fee;
        this.reservationService.edit(reservation, this.selectedReservation.id).subscribe(res => {
          this.logger.success('Success', 'Reservation was saved successfully.');
          this.getMyReservations(0,5);
        }, err => {
          this.logger.error('Error', err.error.message)
        })
      }
    })
    this.reservation = null;
    this.displayDialog = false;
  }

  calculateFee(): void {
    if (this.startDate && this.endDate && this.startTime && this.endTime) {
      let start = new Date(Date.UTC(this.startDate.getFullYear(), this.startDate.getMonth(), this.startDate.getDate(), this.startTime.getHours(), this.startTime.getMinutes()));
      let end = new Date(Date.UTC(this.endDate.getFullYear(), this.endDate.getMonth(), this.endDate.getDate(), this.endTime.getHours(), this.endTime.getMinutes()));
      this.fee = Number((((end.getTime()) - (start.getTime())) * (this.price / 86400000)).toFixed(2));
    }
  }

  defineReservedDates(): void {
    this.reservedDates = [];
    this.reservations.forEach(el => {
      if (el.active === true) {
        let startDate = new Date(el.startDate);
        let endDate = new Date(el.endDate);
        for (let i = startDate.getTime(); i <= endDate.getTime(); i = i + 86400000) {
          this.reservedDates.push(new Date(i));
        }
      }
    })
  }

  delete(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to cancel this reservation?',
      header: 'Accept Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.reservationService.cancel(this.selectedReservation.id).subscribe(res => {
          this.logger.info('Info', 'Reservation was canceled.');
          this.getMyReservations(0,5);
        }, err => {
          this.logger.error('Error', err.error.message);
        })
      }
    })
    this.reservation = null;
    this.displayDialog = false;
  }

  paginate(event): void {
    this.first = event.first;
    this.getMyReservations(event.first, 5,this.carName, this.startFilterDate,this.endFilterDate);
  }

  search(): void {
    this.getMyReservations(this.first, 5, this.carName, this.startFilterDate, this.endFilterDate);
  }

  reset(): void {
    this.carName = null;
    this.startFilterDate = null;
    this.endFilterDate = null;
    this.getMyReservations(this.first, 5, this.carName, this.startFilterDate, this.endFilterDate);
  }

  selectIfAdvanced(): void {
    if (this.advancedFilters === true){
      this.advancedFilters = false;
    } else {
      this.advancedFilters = true;
    }
  }
}
