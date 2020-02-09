import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ConfirmationService, SelectItem } from 'primeng/primeng';
import { Car } from '@ikubinfo/core/models/car';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { Category } from '@ikubinfo/core/models/category';
import { Status } from '@ikubinfo/core/models/status.enum';

@Component({
  selector: 'ikubinfo-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit {

  carForm: FormGroup;
  car: Car;
  file: File;
  brands: SelectItem[];
  categories: Array<Category>;
  diesels: Array<string>;
  editable: boolean;
  selectedType: string = 'AVAILABLE';
  brand: string = "Audi";
  blockSpecial: RegExp = /^[^:>#*]+|([^:>#*][^>#*]+[^:>#*])$/ ;
  types: SelectItem[];
  plate: string;
  constructor(private fb: FormBuilder, private router: Router, private confirmationService: ConfirmationService,
    private carService: CarService, private categoryService: CategoryService,
     private active: ActivatedRoute, private logger: LoggerService) { }

  ngOnInit() {
    this.car = {};
    this.editable = false;
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
      {label: 'Audi', value: 'Audi'},
      {label: 'BMW', value: 'BMW'},
      {label: 'Fiat', value: 'Fiat'},
      {label: 'Ford', value: 'Ford'},
      {label: 'Honda', value: 'Honda'},
      {label: 'Jaguar', value: 'Jaguar'},
      {label: 'Mercedes', value: 'Mercedes'},
      {label: 'Renault', value: 'Renault'},
      {label: 'VW', value: 'VW'},
      {label: 'Volvo', value: 'Volvo'}
  ];
  this.types = [
    {label: 'Available', value: 'AVAILABLE', icon: 'fa fa-check-circle'},
    {label: 'Service', value: 'SERVIS', icon: 'fa fa-wrench'}
];
  this.loadData();
  }

  reset(): void {
    this.fillForm(this.car);
  }

  loadForm(): void {
    this.carForm = this.fb.group({
      name: new FormControl({value:'', disabled:this.editable}, Validators.required),
      photo: [{value:'', disabled:this.editable}, Validators.required],
      description: [{value:'', disabled:this.editable}, [Validators.required, Validators.minLength(50), Validators.maxLength(10000)]],
      year: [{value:'', disabled:this.editable}, [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
      diesel: [{value:'', disabled:this.editable}, Validators.required],
      category: [{value:'', disabled:this.editable}, Validators.required],
      price: [{value:'', disabled:this.editable}, Validators.required]
    })
  }

  fillForm(data: Car = {}): void {
    this.carForm.get('name').setValue(data.name);
    this.carForm.get('description').setValue(data.description);
    this.carForm.get('type').setValue(data.type);
    this.carForm.get('year').setValue(data.year);
    this.carForm.get('diesel').setValue(data.diesel);
    this.carForm.get('availability').setValue(this.selectedType);
    this.carForm.get('category').setValue(data.category.id);
    this.carForm.get('plate').setValue(this.plate);
  }
  
  edit(): void {
    this.reset();
  }

  submit(): void {
    console.log(this.selectedType);
    if (this.car.id !== undefined) {
      this.confirmationService.confirm({
        message: 'Are you sure you want to save the changes?',
        header: 'Accept Confirmation',
        icon: 'pi pi-info-circle',
        accept: () => {
          let formData = new FormData();
          formData.append("file", (this.carForm.get('photo').value || this.car.photo));
          formData.append('model', new Blob([JSON.stringify({
            "name": this.carForm.get('name').value,
            "description": this.carForm.get('description').value,
            "type": this.brand,
            "diesel": this.carForm.get('diesel').value,
            "categoryId": this.carForm.get('category').value,
            "availability": this.selectedType,
            "year": this.carForm.get('year').value,
            "plate": this.plate,
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
          formData.append("properties", new Blob([JSON.stringify({
            "name": this.carForm.get('name').value,
            "description": this.carForm.get('description').value,
            "type": this.brand,
            "diesel": this.carForm.get('diesel').value,
            "categoryId": this.carForm.get('category').value,
            "availability": this.selectedType,
            "year": this.carForm.get('year').value,
            "plate":this.plate,
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
        this.plate = this.car.plate;
        this.carForm.get('price').setValue(this.car.price);
        this.selectedType = this.car.availability.toString();
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

  checkSelected(): void {
    console.log(this.selectedType);
  }
}
