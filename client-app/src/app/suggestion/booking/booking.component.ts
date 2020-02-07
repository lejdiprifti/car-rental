import { Component, OnInit } from '@angular/core';
import { ApiService } from '@ikubinfo/core/utilities/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CarService } from '@ikubinfo/core/services/car.service';
import { Car } from '@ikubinfo/core/models/car';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { Reservation } from '@ikubinfo/core/models/reservation';
import { ReservationService } from '@ikubinfo/core/services/reservation.service';
import { Time, formatDate } from '@angular/common';
import { ConfirmationService } from 'primeng/primeng';

@Component({
  selector: 'ikubinfo-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {

  bookingForm: FormGroup;
  car: Car;
  reservation: Reservation;
  cars: Array<Car>;
  reservedDates: Array<Date>;
  startDate: Date;
  endDate: Date;
  startTime: Date;
  endTime: Date;
  minDate: Date;
  reservations: Array<Reservation>;
  constructor(private fb: FormBuilder, private router: Router,
    private logger: LoggerService, private reservationService: ReservationService,
    private carService: CarService, private active: ActivatedRoute, private confirmationService: ConfirmationService) { }

  ngOnInit() {
    this.bookingForm = this.fb.group({
      car: [{value:'', disabled: true}, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      fee: [{value:'', disabled: true}]
    })
    this.car = {};
    this.reservation = {};
    this.getCarById();
    this.getReservedDatesByCar();
    this.getAllCars();
    this.reservedDates= [];
    this.minDate = new Date();
  }

  getCarById(): void {
    this.carService.getById(Number(this.active.snapshot.paramMap.get("carId"))).subscribe(res => {
      this.car = res;
      this.bookingForm.get('car').setValue(this.car.id);
    }, err=>{
      this.logger.error('Error', 'Car could not be found.');
    })
  }

  getAllCars(): void {
    this.carService.getAll().subscribe(res => {
      this.cars = res;
    }, err=> {
      this.logger.error('Error', 'Cars could not be found.');
    })
  }

  getReservedDatesByCar(): void {
    this.carService.getReservationsByCar(Number(this.active.snapshot.paramMap.get("carId"))).subscribe(res => {
      this.reservations = res;
      this.defineReservedDates();
    }, err=> {
      this.logger.error('Error', 'Reservations could not be found.');
    })
  }

  book(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to reserve the car?',
      header: 'Booking Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
    this.reservation.carId = Number(this.active.snapshot.paramMap.get("carId"));
     let date = new Date(Date.UTC(this.startDate.getFullYear(), this.startDate.getMonth(), this.startDate.getDate(), this.startTime.getHours(), this.startTime.getMinutes()));
    this.reservation.startDate = date;
    this.reservation.fee = this.bookingForm.get('fee').value;
    this.reservation.endDate = new Date(Date.UTC(this.endDate.getFullYear(), this.endDate.getMonth(), this.endDate.getDate(), this.endTime.getHours(), this.endTime.getMinutes()));
    this.reservationService.add(this.reservation).subscribe(res => {
      this.logger.success('Success', 'You reserved the car from ' + formatDate(this.reservation.startDate, 'dd-mm-yyyy hh:mm', 'en-US', 'UTC') +' until '+formatDate(this.reservation.endDate, 'dd-mm-yyyy hh-mm', 'en-US', 'UTC'));
      this.router.navigate(['/rental/cars']);
    }, err=> {
      this.logger.error('Error', err.error.message);
    })
  }
})
  }

  calculateFee(): void {
    if (this.startDate && this.endDate && this.startTime && this.endTime) {
      let start = formatDate(this.startTime, 'hh:mm:a', 'en-US');
      let end = formatDate(this.endTime, 'hh:mm:a', 'en-US');
      let startArray = start.split(':');
      let endArray = end.split(':');
      let startTiming: number;
      if (startArray[2] === 'PM'){
        if (startArray[0] !== '12') {
        startTiming = ((Number(startArray[0])+12 * 3600) + (Number(startArray[1])*60)) * 1000;
        } else {
          startTiming = ((Number(startArray[0]) * 3600) + (Number(startArray[1])*60)) * 1000;
        }
      } else {
        startTiming = ((Number(startArray[0]) * 3600) + (Number(startArray[1])*60)) * 1000;
      }

      let endTiming: number;
      if (endArray[2] === 'PM'){
        if (endArray[0] !== '12') {
        endTiming = ((Number(endArray[0])+12 * 3600) + (Number(endArray[1])*60)) * 1000;
        } else {
          endTiming = ((Number(endArray[0]) * 3600) + (Number(endArray[1])*60)) * 1000;
        }
      } else {
        endTiming = ((Number(endArray[0]) * 3600) + (Number(endArray[1])*60)) * 1000;
        }
      let fee = ((this.endDate.getTime() + endTiming) - (this.startDate.getTime() + startTiming)) * (this.car.price / 86400000);
      this.bookingForm.get('fee').setValue(fee.toFixed(2));
    }
  }

  defineReservedDates(): void {
    this.reservations.forEach(el => {
      let startDate = new Date(el.startDate);
      let endDate = new Date(el.endDate);
      for (let i=startDate.getTime(); i<=endDate.getTime(); i=i+86400000){
        this.reservedDates.push(new Date(i));
      }
    })
  }
}
