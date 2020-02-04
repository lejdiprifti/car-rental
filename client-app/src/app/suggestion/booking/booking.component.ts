import { Component, OnInit } from '@angular/core';
import { ApiService } from '@ikubinfo/core/utilities/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CarService } from '@ikubinfo/core/services/car.service';
import { Car } from '@ikubinfo/core/models/car';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { Reservation } from '@ikubinfo/core/models/reservation';
import { ReservationService } from '@ikubinfo/core/services/reservation.service';
import { Time } from '@angular/common';

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
  reservedDates: Array<Date[]>;
  startDate: Date;
  endDate: Date;
  startTime: Date;
  endTime: Date;
  reservations: Array<Reservation>;
  constructor(private fb: FormBuilder, private router: Router,
    private logger: LoggerService, private reservationService: ReservationService,
    private carService: CarService, private active: ActivatedRoute) { }

  ngOnInit() {
    this.bookingForm = this.fb.group({
      car: [{value:'', disabled: true}, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      fee: ['']
    })
    this.car = {};
    this.getCarById();
    this.getReservedDatesByCar();
    this.getAllCars();
    this.reservedDates= [];
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
      this.reservations.forEach(el => {
        this.reservedDates.push([new Date(el.startDate), new Date(el.endDate)])
      })
    }, err=> {
      this.logger.error('Error', 'Reservations could not be found.');
    })
  }

  book(): void {
    this.reservation.carId = Number(this.active.snapshot.paramMap.get("id"));
    this.reservation.startDate = this.startDate;
    this.reservation.endDate = this.endDate;
    this.reservationService.add(this.reservation).subscribe(res => {
      this.logger.success('Success', 'You reserved the car from' + this.reservation.startDate+' until '+this.reservation.endDate);
      this.router.navigate(['/rental/cars']);
    })
  }

  calculateFee(): void {
    if (this.startDate && this.endDate && this.startTime && this.endTime) {
      let fee = ((this.endDate.getTime() + this.endTime.getMilliseconds()) - (this.startDate.getTime() + this.startTime.getMilliseconds())) * (this.car.price / 86400000);
      this.bookingForm.get('fee').setValue(fee);
    }
  }
}
