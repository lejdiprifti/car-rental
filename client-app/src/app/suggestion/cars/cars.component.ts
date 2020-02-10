import { Component, OnInit } from '@angular/core';
import { Car } from '../../core/models/car';
import { SelectItem } from 'primeng/components/common/selectitem';
import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { MenuItem } from 'primeng/components/common/menuitem';
import { Router } from '@angular/router';
import { ConfirmationService } from 'primeng/primeng';
import { User } from '@ikubinfo/core/models/user';
import { AuthService } from '@ikubinfo/core/services/auth.service';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { Category } from '@ikubinfo/core/models/category';
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
  available: boolean = true;
  today: Date;
  reservedDates: Array<Date>;
  itemsCategories: SelectItem[];
  categories: Array<Category>;
  selectedCategories: any[];

  constructor(private carService: CarService,private categoryService: CategoryService,
     private logger: LoggerService, private router: Router,
    private confirmationService: ConfirmationService, private authService: AuthService) { }

  ngOnInit() {
    this.user= this.authService.user;
    this.loadItems();
    this.loadCars();
    this.loadCategories();
    this.today = new Date();
    this.reservedDates = [];
    this.selectedCategories = [];
    this.sortOptions = [
      { label: 'Newest First', value: '!year' },
      { label: 'Oldest First', value: 'year' },
      { label: 'Brand', value: 'type' },
      { label: 'Availability', value: 'availability'}
    ];
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
    }, err=> {
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

  selectCarToEdit(event: Event, car: Car){
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
          this.logger.error('Error', 'Car could not be deleted.');
        })
      }
    })
  }
  addCar(): void {
    this.router.navigate(['/rental/car']);
  }

  myStyle(): object {
    return {'float': 'right', 'margin-top': '40px'};
  }

  stylePopUp(): object {
    return {'width': '550px', 'height': '500px'};
  }

  checkIfUser(): boolean {
    if (this.user.role.id === 2){ 
      return true;
    } else {
      return false;
    }
  }

  loadItems(): void {
    switch (this.checkIfUser()) {
      case true:
        this.items = [
          {label: 'Book now', icon: 'fa fa-edit', command: () => {
              this.router.navigate(['/rental/reservation/'+this.selectedCar.id])
          }}
      ];
      break;
      case false:
        this.items = [
          {label: 'Update', icon: 'pi pi-refresh', command: () => {
              this.edit(this.selectedCar.id);
          }},
          {label: 'Delete', icon: 'pi pi-times', command: () => {
              this.delete(this.selectedCar);
          }}
      ];
    }
  }

  book(): void {
    this.router.navigate(['/rental/reservation/'+this.selectedCar.id]);
  }

  filterBookings(event): void {
    if (this.startDate && !this.endDate){
      this.cars = this.originalCars;
      this.cars = [];
      this.filterBookingsByStartDate();
    } else if (this.endDate && !this.startDate){
      this.cars = this.originalCars;
      this.cars=[];
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
            if (this.startDate.getTime() >= new Date(set[0]).getTime() && this.startDate.getTime() <= new Date(set[1]).getTime()){
              available = false;
              return;
            }
            return;
          })
          if (available === true){
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
        if (this.endDate.getTime() >= new Date(set[0]).getTime() && this.endDate.getTime() <= new Date(set[1]).getTime()){
          available = false;
          return;
        }
        return;
      })
      if (available === true){
        this.cars.push(el);
      }
    })
  }

  filterBookingsByBoth(): void {
    this.originalCars.forEach(el => {
      this.available = true;
      el.reservedDates.forEach(set => {
        if ((this.startDate.getTime() <= new Date(set[0]).getTime() && this.endDate.getTime() <= new Date(set[0]).getTime()) ||
        (this.startDate.getTime() >= new Date(set[1]).getTime() && this.endDate.getTime() >= new Date(set[1]).getTime())){
          this.available = true;
        } else {
         this.available = false;
        }
      })
      if (this.available === true){
        this.cars.push(el);
      }
    })
  }
  styleUnavailableDates(date: any): object {
    if (this.reservedDates.some(el => new Date(el).getTime() === new Date(date.year, date.month, date.day).getTime())) {
      return {'backgroundColor': 'red'};
    } else {
      return {'backgroundColor': 'inherit'};
    }
  }

  defineReservedDates(): void {
    this.reservedDates = [];
    this.selectedCar.reservedDates.forEach(el => {
      let startDate = new Date(el[0]);
      let endDate = new Date(el[1]);
      for (let i=startDate.getDate(); i<=endDate.getDate(); i=i+1){
        this.reservedDates.push(new Date(endDate.getFullYear(), endDate.getMonth(), i));
      }
    })
  }

  sortByCategory(): void {
    if (this.selectedCategories.length > 0){
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

  checkTodayStatus(car: Car): boolean {
    let now = new Date().getTime();
    let status = true;
    if (car.reservedDates) {
    car.reservedDates.forEach(el => {
      if (new Date(el[0]).getTime() <= now && new Date(el[1]).getTime() >= now){
        status = false;
      } else {
        status = true;
      }
    })
  }
    return status;
  }
}
