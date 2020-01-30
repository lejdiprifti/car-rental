import { Component, OnInit } from '@angular/core';
import { Car } from '../../core/models/car';
import { SelectItem } from 'primeng/components/common/selectitem';
import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { MenuItem } from 'primeng/components/common/menuitem';
import { Router } from '@angular/router';
import { ConfirmationService } from 'primeng/primeng';
@Component({
  selector: 'ikubinfo-cars',
  templateUrl: './cars.component.html',
  styleUrls: ['./cars.component.css']
})
export class CarsComponent implements OnInit {

  cars: Array<Car>;

  selectedCar: Car;
  items: MenuItem[];

  cols: any[];

  constructor(private carService: CarService, private logger: LoggerService, private router: Router,
    private confirmationService: ConfirmationService) { }

  ngOnInit() {
    this.loadCars();
    this.items = [
      { label: 'Edit', icon: 'pi pi-pencil', command: (event) => this.edit(this.selectedCar) },
      { label: 'Delete', icon: 'pi pi-times', command: (event) => this.delete(this.selectedCar) }
    ];

    this.cols = [
<<<<<<< HEAD
      {field: 'photo', header: 'Photo'},
=======
>>>>>>> 933af9bba33acae060737a26a0dc4e93f67d2e32
      { field: 'name', header: 'Name' },
      { field: 'type', header: 'Brand' },
      { field: 'diesel', header: 'Diesel'},
      {field: 'description', header: 'Description'},
      {field: 'availability', header: 'Availability'},
      {field: 'year', header: 'Production Year'},
      {field: 'category', header: 'Category'},
      {field: 'plate', header: 'Plate'},
      {field: 'price', header: 'Price per day'}
    ];

  }

  loadCars(): void {
    this.carService.getAll().subscribe(res => {
      this.cars = res;
    }, err => {
      this.logger.error('Error', 'Cars could not be found.');
    })
  }

  edit(car: Car): void {
    this.router.navigate(['/rental/car/' + car.id]);
  }

  delete(car: Car): void {
    this.confirmationService.confirm({
      message: 'Do you want to delete this car?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.carService.delete(car.id).subscribe(res => {
          this.logger.success('Success', 'Car was deleted successfully!');
          this.loadCars();
        }, err=>{
          this.logger.error('Error', 'Car could not be deleted.');
        })
    }
  })
  }
  addCar(): void {
    this.router.navigate(['/rental/car']);
  }
}
