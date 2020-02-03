import { Component, OnInit } from '@angular/core';
import { CarService } from '@ikubinfo/core/services/car.service';
import { Car } from '@ikubinfo/core/models/car';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';

@Component({
  selector: 'ikubinfo-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent implements OnInit {

  cars: Array<Car>;
  constructor(private carService: CarService, private logger: LoggerService) { }

  ngOnInit() {
    this.loadCars();
  }

  loadCars(): void {
    this.carService.getAll().subscribe(res => {
      this.cars = res;
    }, err=>{
      this.logger.error("Error","Cars were not found.");
    })
  }


}
