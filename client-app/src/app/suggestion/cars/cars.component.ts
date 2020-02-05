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

  constructor(private carService: CarService, private logger: LoggerService, private router: Router,
    private confirmationService: ConfirmationService, private authService: AuthService) { }

  ngOnInit() {
    this.user= this.authService.user;
    this.loadItems();
    this.loadCars();

    this.sortOptions = [
      { label: 'Newest First', value: '!year' },
      { label: 'Oldest First', value: 'year' },
      { label: 'Brand', value: 'type' },
      { label: 'Availability', value: 'availability'}
    ];
  }

  selectCar(event: Event, car: Car) {
        this.selectedCar = car;
        this.displayDialog = true;
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

  loadCars(): void {
    this.carService.getAll().subscribe(res => {
      this.cars = res;
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
    if (this.checkIfUser()){
      this.items = [
        {label: 'Book now', icon: 'fa fa-edit', command: () => {
            this.router.navigate(['/rental/reservation/'+this.selectedCar.id])
        }}
    ];
    } else {
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
}
