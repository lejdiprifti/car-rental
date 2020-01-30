import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ConfirmationService } from 'primeng/primeng';
import { Car } from '@ikubinfo/core/models/car';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { Category } from '@ikubinfo/core/models/category';

@Component({
  selector: 'ikubinfo-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit {

  carForm: FormGroup;
  car: Car;
  file: File;
  brands: Array<string>;
  categories: Array<Category>;
  diesels: Array<string>;
  editable: string;
  available: boolean;
  blockSpecial: RegExp = /^[^:>#*]+|([^:>#*][^>#*]+[^:>#*])$/
  constructor(private fb: FormBuilder, private router: Router, private confirmationService: ConfirmationService,
    private carService: CarService, private categoryService: CategoryService,
     private active: ActivatedRoute, private logger: LoggerService) { }

  ngOnInit() {
    this.car = {};
    this.editable = 'true';
    this.loadForm();
    this.getCategories();
    this.diesels = [
      'Diesel',
      'Petrol',
      'Kerosene',
      'Hydrogen',
      'Biogas'
    ];
    this.brands = [
      'Mercedes-Benz',
      'Ford',
      'Fiat',
      'Honda',
      'KIA',
      'Ferrari',
      'Lamborghini',
      'Chevrolet',
      'Rolls Royce'
  ]
  this.loadData();
  }

  reset(): void {
    this.fillForm(this.car);
  }

  loadForm(): void {
    this.carForm = this.fb.group({
      name: new FormControl({value:'', disabled:this.editable}, Validators.required),
      photo: [{value:'', disabled:this.editable}],
      description: [{value:'', disabled:this.editable}, [Validators.required, Validators.minLength(50), Validators.maxLength(255)]],
      year: [{value:'', disabled:this.editable}, [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
      type: [{value:'', disabled:this.editable}, Validators.required],
      diesel: [{value:'', disabled:this.editable}, Validators.required],
      availability: [{value:'', disabled:this.editable}],
      category: [{value:'', disabled:this.editable}, Validators.required],
      plate: [{value:'', disabled:this.editable}, [ Validators.required, Validators.minLength(5)]],
      price: [{value:'', disabled:this.editable}, Validators.required]
    })
  }

  fillForm(data: Car = {}): void {
    this.carForm.get('name').setValue(data.name);
    this.carForm.get('description').setValue(data.description);
    this.carForm.get('type').setValue(data.type);
    this.carForm.get('year').setValue(data.year);
    this.carForm.get('diesel').setValue(data.diesel);
    this.carForm.get('availability').setValue(this.available);
    this.carForm.get('category').setValue(data.categoryId);
    this.carForm.get('plate').setValue(data.plate);
  }
  
  edit(): void {
    this.editable = 'false';
    this.loadForm();
    this.reset();
  }

  submit(): void {
    if (this.car.id !== undefined) {
      this.confirmationService.confirm({
        message: 'Are you sure you want to save the changes?',
        header: 'Accept Confirmation',
        icon: 'pi pi-info-circle',
        accept: () => {
          let formData = new FormData();
          formData.append("file", (this.carForm.get('photo').value || this.car.photo));
          formData.append('properties', new Blob([JSON.stringify({
            "name": this.carForm.get('name').value,
            "description": this.carForm.get('description').value,
            "type": this.carForm.get('type').value,
            "diesel": this.carForm.get('diesel').value,
            "categoryId": this.carForm.get('category').value,
            "availability": this.available,
            "year": this.carForm.get('year').value,
            "plate": this.carForm.get('plate').value,
            "price": this.carForm.get('price').value
          })], {
            type: "application/json"
          }));
          this.carService.edit(formData, this.car.id).subscribe(res => {
            this.router.navigate(['rental/cars']);
            this.logger.success("Success", "Data saved successfully!");
          }, err => {
            this.logger.error("Error", "Car plate already exists.");
          });
        }
        })
    }
    else {
      this.confirmationService.confirm({
        message: 'Are you sure you want to add this category?',
        header: 'Accept Confirmation',
        icon: 'pi pi-info-circle',
        accept: () => {
          let formData = new FormData();
          formData.append("file", (this.carForm.get('photo').value || this.car.photo));
          formData.append('properties', new Blob([JSON.stringify({
            "name": this.carForm.get('name').value,
            "description": this.carForm.get('description').value,
            "type": this.carForm.get('type').value,
            "diesel": this.carForm.get('diesel').value,
            "categoryId": this.carForm.get('category').value,
            "availability": this.available,
            "year": this.carForm.get('year').value,
            "plate": this.carForm.get('plate').value,
            "price": this.carForm.get('price').value
          })], {
            type: "application/json"
          }));
          return this.carService.add(formData).subscribe(res => {
            this.router.navigate(['rental/dashboard']);
            this.logger.success("Success", "Car was successfully created.");
          }, err => {
            this.logger.error("Error", "Car name already exists.");
          });
        }
      });
    }
  }
 
  loadData(): void {
    const id = this.active.snapshot.paramMap.get('id');
    if (id) {
      this.carService.getById(Number(id)).subscribe(res => {
        this.car = res;
        this.carForm.get('name').setValue(this.car.name);
        this.carForm.get('description').setValue(this.car.description);
        this.carForm.get('year').setValue(this.car.year);
        this.carForm.get('diesel').setValue(this.car.diesel);
        this.carForm.get('category').setValue(this.car.categoryId);
        this.carForm.get('type').setValue(this.car.type);
        this.carForm.get('plate').setValue(this.car.plate);
        this.carForm.get('price').setValue(this.car.price);
        this.available = this.car.availability;
      },
        err => {
          this.logger.error("Error", "Something bad happened.");
        });
    }
  }

  uploadFile(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.carForm.get('photo').setValue(file);
    }
  }

  getCategories(): void {
    this.categoryService.getAll().subscribe(res => {
      this.categories = res
    }, err=> {
      this.logger.error('Error','Categories were not found.');
    })
  }
}
