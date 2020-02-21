import { Component, OnInit } from "@angular/core";
import { formatDate } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

import { CarService } from "@ikubinfo/core/services/car.service";
import { Car } from "@ikubinfo/core/models/car";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { Reservation } from "@ikubinfo/core/models/reservation";
import { ReservationService } from "@ikubinfo/core/services/reservation.service";
import { cols } from '@ikubinfo/suggestion/booking/booking.constants';

import { ConfirmationService } from "primeng/primeng";

@Component({
  selector: "ikubinfo-booking",
  templateUrl: "./booking.component.html",
  styleUrls: ["./booking.component.css"]
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
  cols: any[];
  reservations: Array<Reservation>;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private logger: LoggerService,
    private reservationService: ReservationService,
    private carService: CarService,
    private active: ActivatedRoute,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.bookingForm = this.fb.group({
      car: [{ value: "", disabled: true }, Validators.required],
      startDate: ["", Validators.required],
      endDate: ["", Validators.required],
      startTime: ["", Validators.required],
      endTime: ["", Validators.required],
      fee: [{ value: "", disabled: true }]
    });
    this.car = {};
    this.reservation = {};
    this.getCarById();
    this.getReservedDatesByCar();
    this.reservedDates = [];
    this.minDate = new Date();
    this.cols = cols;
  }

  getCarById(): void {
    this.carService
      .getById(Number(this.active.snapshot.paramMap.get("carId")))
      .subscribe(
        res => {
          this.car = res;
          this.bookingForm
            .get("car")
            .setValue(this.car.type + " " + this.car.name);
        },
        err => {
          this.logger.error("Error", "Car could not be found.");
        }
      );
  }

  getReservedDatesByCar(): void {
    this.carService
      .getReservationsByCar(Number(this.active.snapshot.paramMap.get("carId")))
      .subscribe(
        res => {
          this.reservations = res;
        },
        err => {
          this.logger.error("Error", "Reservations could not be found.");
        }
      );
  }

  book(): void {
    this.confirmationService.confirm({
      message: "Are you sure you want to reserve the car?",
      header: "Booking Confirmation",
      icon: "pi pi-info-circle",
      accept: () => {
        this.reservation.carId = Number(
          this.active.snapshot.paramMap.get("carId")
        );
        let date = new Date(
          Date.UTC(
            this.startDate.getFullYear(),
            this.startDate.getMonth(),
            this.startDate.getDate(),
            this.startTime.getHours(),
            this.startTime.getMinutes()
          )
        );
        this.reservation.startDate = date;
        this.reservation.fee = this.bookingForm.get("fee").value;
        this.reservation.endDate = new Date(
          Date.UTC(
            this.endDate.getFullYear(),
            this.endDate.getMonth(),
            this.endDate.getDate(),
            this.endTime.getHours(),
            this.endTime.getMinutes()
          )
        );
        this.reservationService.add(this.reservation).subscribe(
          res => {
            this.logger.success(
              "Success",
              "You reserved the car from " +
                formatDate(
                  this.reservation.startDate,
                  "dd-MM-yyyy hh:mm",
                  "en-US",
                  "UTC"
                ) +
                " until " +
                formatDate(
                  this.reservation.endDate,
                  "dd-MM-yyyy hh:mm",
                  "en-US",
                  "UTC"
                )
            );
            this.router.navigate(["/rental/cars"]);
          },
          err => {
            this.logger.error("Error", err.error.message);
          }
        );
      }
    });
  }

  calculateFee(): void {
    if (this.startDate && this.endDate && this.startTime && this.endTime) {
      let startDate = new Date(
        Date.UTC(
          this.startDate.getFullYear(),
          this.startDate.getMonth(),
          this.startDate.getDate(),
          this.startTime.getHours(),
          this.startTime.getMinutes()
        )
      );
      let endDate = new Date(
        Date.UTC(
          this.endDate.getFullYear(),
          this.endDate.getMonth(),
          this.endDate.getDate(),
          this.endTime.getHours(),
          this.endTime.getMinutes()
        )
      );
      let fee =
        (endDate.getTime() - startDate.getTime()) * (this.car.price / 86400000);
      this.bookingForm.get("fee").setValue(fee.toFixed(2));
    }
  }
}
