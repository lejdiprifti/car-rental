import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CarService } from '@ikubinfo/core/services/car.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ConfirmationService } from 'primeng/primeng';
import { Car } from '@ikubinfo/core/models/car';

@Component({
  selector: 'ikubinfo-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.css']
})
export class CarComponent implements OnInit {

  carForm: FormGroup;
  car: Car;
  file: File;
  constructor(private fb: FormBuilder, private router: Router, private confirmationService: ConfirmationService,
    private carService: CarService, private active: ActivatedRoute, private logger: LoggerService) { }

  ngOnInit() {
    this.car = {};
    this.carForm = this.fb.group({
      name: ['', Validators.required],
      photo: [''],
      description: ['', [Validators.required, Validators.minLength(50), Validators.maxLength(255)]],
      year: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
      type: ['', Validators.required],
      diesel: ['', Validators.required],
      availability: [''],
      categoryId: ['', Validators.required]
    })
  }

  reset(): void {
    this.fillForm(this.car);
  }

  fillForm(data: Car = {}): void {
    this.carForm.get('name').setValue(data.name);
    this.carForm.get('description').setValue(data.description);
    this.carForm.get('type').setValue(data.type);
    this.carForm.get('year').setValue(data.year);
    this.carForm.get('diesel').setValue(data.diesel);
    this.carForm.get('availability').setValue(data.availability);
    this.carForm.get('category').setValue(data.categoryId);
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
        this.carForm.get('availability').setValue(this.car.availability);
        this.carForm.get('category').setValue(this.car.categoryId);
        this.carForm.get('type').setValue(this.car.type);
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
}
