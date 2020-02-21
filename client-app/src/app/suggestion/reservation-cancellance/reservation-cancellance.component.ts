import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ReservationService } from '@ikubinfo/core/services/reservation.service';
import { ConfirmationService } from 'primeng/primeng';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { Car } from '@ikubinfo/core/models/car';
import { CarService } from '@ikubinfo/core/services/car.service';
import { cols } from './reservation-cancellance.constants';
import { Status } from '@ikubinfo/core/models/status.enum';

@Component({
  selector: 'ikubinfo-reservation-cancellance',
  templateUrl: './reservation-cancellance.component.html',
  styleUrls: ['./reservation-cancellance.component.css']
})
export class ReservationCancellanceComponent implements OnInit {
  car: Car;
  startTime: Date;
  startDate: Date;
  cols: any[];
  minDate: Date;
  checked: boolean;
  constructor(private router: Router, private logger: LoggerService, private active: ActivatedRoute,
    private confirmationService: ConfirmationService, private carService: CarService) { }

  ngOnInit() {
    this.car={};
    this.loadCar();
    this.cols = cols;
    this.minDate = new Date();
    this.checked = true;
  }

  loadCar(): void {
    const carId = Number(this.active.snapshot.paramMap.get("carId"));
    this.carService.getById(carId).subscribe(res => {
      this.car = res;
    }, err=>{
      this.logger.error('Error', err.error.message);
    })
  }

  cancelReservations(): void {
    if (this.car.availability.toString() === "SERVIS"){
    let date;
    const carId = Number(this.active.snapshot.paramMap.get("carId"));
    if (this.checked === false){
     date = new Date(Date.UTC(this.startDate.getFullYear(), this.startDate.getMonth(), this.startDate.getDate(), this.startTime.getHours(), this.startTime.getMinutes()));
    } else {
      date = new Date();
    }
    this.confirmationService.confirm({
      message: "Are you sure you want to cancel the reservations?",
      header: "Save Confirmation",
      icon: "pi pi-info-circle",
      accept: () => {
        this.carService.cancelByCarAndDate(date, carId).subscribe(res => {
          this.logger.info('Information', res + ' reservations were successfully canceled.');
          this.router.navigate(['/rental/cars']);
        }, err=>{
          this.logger.error('Error', err.error.message);
        });
      }
    });
  }
  }

}
