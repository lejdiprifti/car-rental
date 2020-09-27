import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { CarService } from "@ikubinfo/core/services/car.service";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { User } from "@ikubinfo/core/models/user";
import { AuthService } from "@ikubinfo/core/services/auth.service";
import { CategoryService } from "@ikubinfo/core/services/category.service";
import { Category } from "@ikubinfo/core/models/category";

import { ConfirmationService } from "primeng/primeng";
import { SelectItem } from "primeng/components/common/selectitem";
import { MenuItem } from "primeng/components/common/menuitem";
import { Car } from '@ikubinfo/core/models/car';

@Component({
  selector: "ikubinfo-cars",
  templateUrl: "./cars.component.html",
  styleUrls: ["./cars.component.css"]
})
export class CarsComponent implements OnInit {
  cars: Array<Car>;

  selectedCar: Car;

  displayDialog: boolean;

  items: MenuItem[];

  user: User;

  date: Date;

  first: number = 0;

  originalCars: Array<Car>;

  endDate: Date;

  startDate: Date;

  available: boolean;

  today: Date;

  reservedDates: Array<Date>;

  itemsCategories: SelectItem[];

  categories: Array<Category>;

  selectedCategories: any[];

  totalRecords: number;

  selectedCategoryIds: number[];

  advancedFilters: boolean = false;

  brand: string;
  constructor(
    private carService: CarService,
    private categoryService: CategoryService,
    private logger: LoggerService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.user = this.authService.user;
    this.today = new Date();
    this.reservedDates = [];
    this.selectedCategories = [];
    this.itemsCategories = [];
    this.selectedCategoryIds = [];
    this.loadItems();
    this.loadCategories();
    this.loadCars(0, 4, this.selectedCategoryIds, this.startDate, this.endDate);
  }

  selectCar(event: Event, car: Car) {
    this.selectedCar = car;
    this.displayDialog = true;
    this.defineReservedDates();
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedCar = null;
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(
      res => {
        this.categories = res;
        this.categories.forEach(el => {
          this.addCategoryItems(el);
        });
      },
      err => {
        this.logger.error("Error", err.error.message);
      }
    );
  }

  addCategoryItems(category: Category): void {
    this.itemsCategories.push({
      value: {
        photo: category.photo,
        name: category.name,
        id: category.id
      },
      label: category.name
    });
  }

  loadCars(
    startIndex: number,
    pageSize: number,
    selectedCategories?: any[],
    startDate?: Date,
    endDate?: Date,
    brand?: string
  ): void {
    this.carService
      .getAll(startIndex, pageSize, selectedCategories, startDate, endDate,brand)
      .subscribe(
        res => {
          this.cars = res.carsList;
          this.totalRecords = res.totalRecords;
          this.originalCars = this.cars;
        },
        err => {
          this.logger.error("Error", "Cars could not be found.");
        }
      );
  }
  selectCarToEdit(event: Event, car: Car) {
    this.selectedCar = car;
    event.preventDefault();
  }

  edit(id: number): void {
    this.router.navigate(["/rental/car/" + id]);
  }

  delete(car: Car): void {
    this.confirmationService.confirm({
      message: "Do you want to delete this car?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      accept: () => {
        this.carService.delete(car.id).subscribe(
          res => {
            this.logger.success("Success", "Car was deleted successfully!");
            this.loadCars(0, 4, this.selectedCategoryIds, this.startDate,this.endDate);
          },
          err => {
            this.logger.error("Error", err.error.message);
          }
        );
      }
    });
  }
  addCar(): void {
    this.router.navigate(["/rental/car"]);
  }

  myStyle(): object {
    return { float: "right", "margin-top": "40px" };
  }

  stylePopUp(): object {
    return { width: "650px", height: "600px" };
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
            label: "Book now",
            icon: "fa fa-edit",
            command: () => {
              this.router.navigate([
                "/rental/car/" + this.selectedCar.id + "/reservation"
              ]);
            }
          }
        ];
        break;
      case 1:
        this.items = [
          {
            label: "Update",
            icon: "pi pi-refresh",
            command: () => {
              this.edit(this.selectedCar.id);
            }
          },
          {
            label: "Delete",
            icon: "pi pi-times",
            command: () => {
              this.delete(this.selectedCar);
            }
          }
        ];
    }
  }

  book(): void {
    this.router.navigate([
      "/rental/car/" + this.selectedCar.id + "/reservation"
    ]);
  }

  styleUnavailableDates(date: any): object {
    if (
      this.reservedDates.some(
        el =>
          new Date(el).getTime() ===
          new Date(date.year, date.month, date.day).getTime()
      )
    ) {
      return { backgroundColor: "red" };
    } else {
      return { backgroundColor: "inherit" };
    }
  }

  defineReservedDates(): void {
    this.reservedDates = [];
    this.selectedCar.reservedDates.forEach(el => {
      let startDate = new Date(el[0]);
      let endDate = new Date(el[1]);
      for (let i = startDate.getDate(); i <= endDate.getDate(); i = i + 1) {
        this.reservedDates.push(
          new Date(endDate.getFullYear(), endDate.getMonth(), i)
        );
      }
    });
  }

  sortByCategory(): void {
    if (this.selectedCategories.length > 0) {
      this.selectedCategoryIds = [];
      this.selectedCategories.forEach(el => {
        this.selectedCategoryIds.push(el.id);
      });
    } else if (this.selectedCategories.length === 0) {
      this.selectedCategoryIds = [];
    }
  }
  checkTodayStatus(car: Car): string {
    switch (this.user.role.id) {
      case 1:
        if (
          car.availability.toString() == "RENTED" ||
          car.availability.toString() == "AVAILABLE"
        ) {
          let now = new Date().getTime();
          let status = "AVAILABLE";
          if (car.reservedDates) {
            car.reservedDates.forEach(el => {
              if (
                new Date(el[0]).getTime() <= now &&
                new Date(el[1]).getTime() >= now
              ) {
                status = "RENTED";
              }
            });
          }
          return status;
        }
        return "SERVIS";
      case 2:
        let now = new Date().getTime();
        let status = "AVAILABLE";
        if (car.reservedDates) {
          car.reservedDates.forEach(el => {
            if (
              new Date(el[0]).getTime() <= now &&
              new Date(el[1]).getTime() >= now
            ) {
              status = "RENTED";
            }
          });
        }
        return status;
    }
  }

  cancelBookings(carId: number): void {
    this.router.navigate(["/rental/car/" + carId + "/reservations/cancel"]);
  }

  paginate(event) {
    this.first = event.first;
    this.loadCars(
      event.first,
      4,
      this.selectedCategoryIds,
      this.startDate,
      this.endDate,
      this.brand
    );
  }

  search(): void {
    this.loadCars(
      this.first,
      4,
      this.selectedCategoryIds,
      this.startDate,
      this.endDate,
      this.brand
    );
  }

  reset(): void {
    this.selectedCategories = [];
    this.selectedCategoryIds = [];
    this.startDate = null;
    this.endDate = null;
    this.brand=null;
    this.loadCars(
      this.first,
      4,
      this.selectedCategoryIds,
      this.startDate,
      this.endDate
    );
  }

  selectIfAdvanced(): void {
    if (this.advancedFilters === true) {
      this.advancedFilters = false;
    } else {
      this.advancedFilters = true;
    }
  }
}
