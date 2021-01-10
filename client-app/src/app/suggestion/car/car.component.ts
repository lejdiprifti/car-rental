import { Component, OnInit } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormControl
} from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";

import { CarService } from "@ikubinfo/core/services/car.service";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { Car } from "@ikubinfo/core/models/car";
import { CategoryService } from "@ikubinfo/core/services/category.service";
import { Category } from "@ikubinfo/core/models/category";
import { Status } from "@ikubinfo/core/models/status.enum";
import { diesels, brands, types } from "@ikubinfo/suggestion/car/car.constants";

import { ConfirmationService, SelectItem } from "primeng/primeng";
import { prepareFormData } from "../util";
@Component({
  selector: "ikubinfo-car",
  templateUrl: "./car.component.html",
  styleUrls: ["./car.component.css"]
})
export class CarComponent implements OnInit {
  carForm: FormGroup;
  car: Car;
  file: File;
  brands: SelectItem[];
  categories: Array<Category>;
  diesels: Array<string>;
  editable: boolean;
  blockSpecial: RegExp = /^[^:>#*]+|([^:>#*][^>#*]+[^:>#*])$/;
  types: SelectItem[];
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private confirmationService: ConfirmationService,
    private carService: CarService,
    private categoryService: CategoryService,
    private active: ActivatedRoute,
    private logger: LoggerService
  ) {}

  ngOnInit() {
    this.car = {};
    this.editable = false;
    this.loadForm();
    this.getCategories();
    this.diesels = diesels;
    this.brands = brands;
    this.types = types;
    this.loadData();
  }

  reset(): void {
    this.fillForm(this.car);
  }

  loadForm(): void {
    this.carForm = this.fb.group({
      name: new FormControl(
        { value: "", disabled: this.editable },
        Validators.required
      ),
      photo: [{ value: "", disabled: this.editable }],
      description: [
        { value: "", disabled: this.editable },
        [
          Validators.required,
          Validators.minLength(50),
          Validators.maxLength(10000)
        ]
      ],
      year: [
        { value: "", disabled: this.editable },
        [Validators.required, Validators.minLength(4), Validators.maxLength(4)]
      ],
      diesel: [{ value: "", disabled: this.editable }, Validators.required],
      category: [{ value: "", disabled: this.editable }, Validators.required],
      price: [{ value: "", disabled: this.editable }, Validators.required],
      brand: ["", Validators.required],
      plate: ["", [Validators.required, Validators.minLength(7)]],
      availability: ["", Validators.required]
    });
  }

  fillForm(data: Car = {}): void {
    this.carForm.get("name").setValue(data.name);
    this.carForm.get("description").setValue(data.description);
    this.carForm.get("year").setValue(data.year);
    this.carForm.get("diesel").setValue(data.diesel);
    this.carForm.get("category").setValue(data.categoryId);
    this.carForm.get("brand").setValue(data.type);
    this.carForm.get("plate").setValue(data.plate);
    this.carForm.get("availability").setValue(data.availability);
    this.carForm.get("price").setValue(data.price);
  }

  edit(): void {
    this.reset();
  }

  submit(): void {
    if (this.file) {
      if (this.car.id !== undefined) {
        this.confirmationService.confirm({
          message: "Are you sure you want to save the changes?",
          header: "Accept Confirmation",
          icon: "pi pi-info-circle",
          accept: () => {
            let formData = prepareFormData(
              this.file,
              new Blob(
                [
                  JSON.stringify({
                    id: this.car.id,
                    name: this.carForm.get("name").value,
                    description: this.carForm.get("description").value,
                    type: this.carForm.get("brand").value || "Audi",
                    diesel: this.carForm.get("diesel").value,
                    categoryId: this.carForm.get("category").value,
                    availability:
                      this.carForm.get("availability").value ||
                      Status.AVAILABLE,
                    year: this.carForm.get("year").value,
                    plate: this.carForm.get("plate").value,
                    price: this.carForm.get("price").value
                  })
                ],
                {
                  type: "application/json"
                }
              )
            );
            this.carService.edit(formData, this.car.id).subscribe(
              res => {
                this.router.navigate(["rental/cars"]);
                this.logger.success("Success", "Data saved successfully!");
              },
              err => {
                this.logger.error("Error", err.error.message);
              }
            );
          }
        });
      } else {
        this.confirmationService.confirm({
          message: "Are you sure you want to add this car?",
          header: "Accept Confirmation",
          icon: "pi pi-info-circle",
          accept: () => {
            let formData = new FormData();
            formData.append("file", this.file);
            formData.append(
              "properties",
              new Blob(
                [
                  JSON.stringify({
                    name: this.carForm.get("name").value,
                    description: this.carForm.get("description").value,
                    type: this.carForm.get("brand").value || "Audi",
                    diesel: this.carForm.get("diesel").value,
                    categoryId: this.carForm.get("category").value,
                    availability:
                      this.carForm.get("availability").value ||
                      Status.AVAILABLE,
                    year: this.carForm.get("year").value,
                    plate: this.carForm.get("plate").value,
                    price: this.carForm.get("price").value
                  })
                ],
                {
                  type: "application/json"
                }
              )
            );
            return this.carService.add(formData).subscribe(
              res => {
                this.router.navigate(["rental/cars"]);
                this.logger.success("Success", "Car was successfully created.");
              },
              err => {
                this.logger.error("Error", err.error.message);
              }
            );
          }
        });
      }
    } else {
      this.logger.warning("Warning!", "Please select a photo for this car!");
    }
  }

  loadData(): void {
    const id = this.active.snapshot.paramMap.get("id");
    if (id) {
      this.carService.getById(Number(id)).subscribe(
        res => {
          this.car = res;
          this.file = this.car.photo;
          this.carForm.get("name").setValue(this.car.name);
          this.carForm.get("description").setValue(this.car.description);
          this.carForm.get("year").setValue(this.car.year);
          this.carForm.get("diesel").setValue(this.car.diesel);
          this.carForm.get("category").setValue(this.car.categoryId);
          this.carForm.get("brand").setValue(this.car.type);
          this.carForm.get("availability").setValue(this.car.availability);
          this.carForm.get("price").setValue(this.car.price);
          this.carForm.get("plate").setValue(this.car.plate);
        },
        err => {
          this.logger.error("Error", "Something bad happened.");
        }
      );
    }
  }

  uploadFile(event) {
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
    }
  }

  getCategories(): void {
    this.categoryService.getAllCategories().subscribe(
      res => {
        this.categories = res;
      },
      err => {
        this.logger.error("Error", "Categories were not found.");
      }
    );
  }
}
