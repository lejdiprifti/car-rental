import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { User } from '@ikubinfo/core/models/user';
import { AuthService } from '@ikubinfo/core/services/auth.service';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { Category } from '@ikubinfo/core/models/category';
import { Car } from '@ikubinfo/core/models/car';
import { sortOptions } from "@ikubinfo/suggestion/cars/cars.constants";

import { ConfirmationService } from 'primeng/primeng';
import { SelectItem } from 'primeng/components/common/selectitem';
import { MenuItem } from 'primeng/components/common/menuitem';

@Component({
  selector: 'ikubinfo-cars',
  templateUrl: './cars.component.html',
  styleUrls: ['./cars.component.css']
})
export class CarsComponent implements OnInit {

  cars: Array<Car>;

  selectedCar: Car;

  displayDialog: boolean;

  sortOptions: SelectItem[];

  sortKey: string;

  sortField: string;

  sortOrder: number;

  items: MenuItem[];

  user: User;

  date: Date;

  originalCars: Array<Car>;

  endDate: Date;

  startDate: Date;
  
  available: boolean;
  
  today: Date;
  
  reservedDates: Array<Date>;
  
  itemsCategories: SelectItem[];
  
  categories: Array<Category>;
  
  selectedCategories: any[];
  
  constructor(private carService: CarService, private categoryService: CategoryService,
    private logger: LoggerService, private router: Router,
    private confirmationService: ConfirmationService, private authService: AuthService) { }

  ngOnInit() {
    this.user = this.authService.user;
    this.loadItems();
    this.loadCars();
    this.loadCategories();
    this.today = new Date();
    this.reservedDates = [];
    this.selectedCategories = [];
    this.sortOptions = sortOptions;
    this.itemsCategories = [];
  }

  selectCar(event: Event, car: Car) {
    this.selectedCar = car;
    this.displayDialog = true;
    this.defineReservedDates();
    event.preventDefault();
  }

  onSortChange(event) {
    let value = event.value;

    if (value.indexOf('!') === 0) {
      this.sortOrder = -1;
      this.sortField = value.substring(1, value.length);
    }
    else {
      this.sortOrder = 1;
      this.sortField = value;
    }
  }

  onDialogHide() {
    this.selectedCar = null;
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe(res => {
      this.categories = res;
      this.categories.forEach(el => {
        this.addCategoryItems(el);
      })
    }, err => {
      this.logger.error('Error', 'Categories could not be found.');
    })
  }

  addCategoryItems(category: Category): void {
    this.itemsCategories.push({
      value: {
        photo: category.photo,
        name: category.name,
        id: category.id
      }, label: category.name,
    })
  }

  loadCars(): void {
    this.carService.getAll().subscribe(res => {
      this.cars = res;
      this.originalCars = this.cars;
    }, err => {
      this.logger.error('Error', 'Cars could not be found.');
    })
  }
  selectCarToEdit(event: Event, car: Car) {
    this.selectedCar = car;
    event.preventDefault();
  }

  edit(id: number): void {
    this.router.navigate(['/rental/car/' + id]);
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
        }, err => {
          this.logger.error('Error', err.error.message);
        })
      }
    })
  }
  addCar(): void {
    this.router.navigate(['/rental/car']);
  }

  myStyle(): object {
    return { 'float': 'right', 'margin-top': '40px' };
  }

  stylePopUp(): object {
    return { 'width': '650px', 'height': '600px' };
  }

  checkIfUser(): boolean {
    if (this.user.role.id === 2) {
      return true;
    } else {
      return false;
    }
  }

  loadItems(): void {
    switch (this.user.role.id) {
      case 2:
        this.items = [
          {
            label: 'Book now', icon: 'fa fa-edit', command: () => {
              this.router.navigate(['/rental/car/' + this.selectedCar.id+'/reservation'])
            }
          }
        ];
        break;
      case 1:
        this.items = [
          {
            label: 'Update', icon: 'pi pi-refresh', command: () => {
              this.edit(this.selectedCar.id);
            }
          },
          {
            label: 'Delete', icon: 'pi pi-times', command: () => {
              this.delete(this.selectedCar);
            }
          }
        ];
    }
  }

  book(): void {
    this.router.navigate(['/rental/car/' + this.selectedCar.id+'/reservation']);
  }

  filterBookings(): void {
    if (this.startDate && !this.endDate) {
      this.cars = this.originalCars;
      this.cars = [];
      this.filterBookingsByStartDate();
    } else if (this.endDate && !this.startDate) {
      this.cars = this.originalCars;
      this.cars = [];
      this.filterBookingsByEndDate();
    } else if (this.endDate && this.startDate) {
      this.cars = this.originalCars;
      this.cars = [];
      this.filterBookingsByBoth();
    } else {
      this.cars = this.originalCars;
    }
  }

  filterBookingsByStartDate(): void {
    this.originalCars.forEach(el => {
      let available: boolean = true;
      switch (el.reservedDates.length) {
        case 0:
          this.cars.push(el);
          break;
        default:
          el.reservedDates.forEach(set => {
            let start = new Date(set[0]);
            let end = new Date(set[1]);
            let startDate = new Date(start.getFullYear(), start.getMonth(), start.getDate());
            let endDate = new Date(end.getFullYear(), end.getMonth(), end.getDate());
            if (this.startDate.getTime() >= startDate.getTime() && this.startDate.getTime() <= endDate.getTime()) {
              available = false;
              return;
            }
            return;
          })
          if (available === true) {
            this.cars.push(el);
          }
          break;
      }
    })
  }

  filterBookingsByEndDate(): void {
    this.originalCars.forEach(el => {
      let available: boolean = true;
      el.reservedDates.forEach(set => {
        let start = new Date(set[0]);
        let end = new Date(set[1]);
        let startDate = new Date(start.getFullYear(), start.getMonth(), start.getDate());
        let endDate = new Date(end.getFullYear(), end.getMonth(), end.getDate());
        if (this.endDate.getTime() >= startDate.getTime()
          && this.endDate.getTime() <= endDate.getTime()) {
          available = false;
          return;
        }
        return;
      })
      if (available === true) {
        this.cars.push(el);
      }
    })
  }

  filterBookingsByBoth(): void {
    this.originalCars.forEach(el => {
      this.available = true;
      el.reservedDates.forEach(set => {
        let start = new Date(set[0]);
        let end = new Date(set[1]);
        let startDate = new Date(start.getFullYear(), start.getMonth(), start.getDate());
        let endDate = new Date(end.getFullYear(), end.getMonth(), end.getDate());
        if (!((this.startDate.getTime() < startDate.getTime() && this.endDate.getTime() < startDate.getTime()) ||
          (this.startDate.getTime() > endDate.getTime() && this.endDate.getTime() > endDate.getTime()))) {
          this.available = false;
        }
      })
      if (this.available === true) {
        this.cars.push(el);
      }
    })
  }
  styleUnavailableDates(date: any): object {
    if (this.reservedDates.some(el => new Date(el).getTime() === new Date(date.year, date.month, date.day).getTime())) {
      return { 'backgroundColor': 'red' };
    } else {
      return { 'backgroundColor': 'inherit' };
    }
  }

  defineReservedDates(): void {
    this.reservedDates = [];
    this.selectedCar.reservedDates.forEach(el => {
      let startDate = new Date(el[0]);
      let endDate = new Date(el[1]);
      for (let i = startDate.getDate(); i <= endDate.getDate(); i = i + 1) {
        this.reservedDates.push(new Date(endDate.getFullYear(), endDate.getMonth(), i));
      }
    })
  }

  sortByCategory(): void {
    if (this.selectedCategories.length > 0) {
      this.cars = [];
      this.originalCars.filter(el => {
        this.selectedCategories.forEach(cat => {
          if (el.category.id === cat.id) {
            this.cars.push(el);
          }
        })
      })
    } else {
      this.cars = this.originalCars;
    }
  }

  checkTodayStatus(car: Car): string {
    switch (this.user.role.id) {
      case 1:
        if (car.availability.toString() == 'RENTED' || car.availability.toString() == 'AVAILABLE') {
          let now = new Date().getTime();
          let status = "AVAILABLE";
          if (car.reservedDates) {
            car.reservedDates.forEach(el => {
              if (new Date(el[0]).getTime() <= now && new Date(el[1]).getTime() >= now) {
                status = "RENTED";
              }
            })
          }
          return status;
        }
        return "SERVIS";
      case 2:
        let now = new Date().getTime();
        let status = "AVAILABLE";
        if (car.reservedDates) {
          car.reservedDates.forEach(el => {
            if (new Date(el[0]).getTime() <= now && new Date(el[1]).getTime() >= now) {
              status = "RENTED";
            }
          })
        }
        return status;
    }
  }

  cancelBookings(carId: number): void {
    this.router.navigate(['/rental/car/'+carId+'/reservations/cancel']);
  }
}
