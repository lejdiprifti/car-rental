import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from '@ikubinfo/core/models/category';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ConfirmationService } from 'primeng/primeng';

@Component({
  selector: 'ikubinfo-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
  categoryForm: FormGroup;
  category: Category;
  file: File;
  constructor(private fb: FormBuilder, private router: Router, private logger: LoggerService,
    private categoryService: CategoryService,private active: ActivatedRoute, private confirmationService: ConfirmationService) { }
  ngOnInit() {
    this.category = {};
    this.categoryForm=this.fb.group({
      name: ['' , Validators.required] ,
      photo: ['', Validators.required],
      description: ['', Validators.required],
    });
  }
  reset(): void {
    this.fillForm(this.category);
  }

  submit(): void {
    if (!this.category){
      this.categoryService.edit(this.getData(), this.category.id).subscribe(res=>{
        this.router.navigate(['rental/dashboard']);
        this.logger.success("Success", "Data saved successfully!");
      }, err=>{
        this.logger.error("Error", "Category name exists.");
      });
    }
    else{
      this.confirmationService.confirm({
        message: 'Are you sure you want to add this category?',
        header: 'Accept Confirmation',
        icon: 'pi pi-info-circle',
        accept: () => {
        let formData = new FormData();
        formData.append("file", this.categoryForm.get('photo').value);
        formData.append('properties', new Blob([JSON.stringify({
                        "name": this.categoryForm.get('name').value,
                        "description": this.categoryForm.get('description').value                    
                    })], {
                        type: "application/json"
                    }));
      return this.categoryService.save(formData).subscribe(res=>{
        this.router.navigate(['rental/dashboard']);
        this.logger.success("Success", "Category was successfully created.");
      }, err=>{
        this.logger.error("Error", "Category name already exists.");
      });
    }
  });
    }
  }
  getData(): Category {
    return {
      name: this.categoryForm.get('name').value,
      description: this.categoryForm.get('description').value,
      photo: this.categoryForm.get('photo').value
    }

  }
  fillForm(data: Category={}): void{
    this.categoryForm.get('name').setValue(data.name);
    this.categoryForm.get('description').setValue(data.description);
    this.categoryForm.get('photo').setValue(data.photo);
  }

  loadData(): void{ 
    const id = this.active.snapshot.paramMap.get('id');
    if (id) {
     this.categoryService.getById(Number(id)).subscribe(res=>{
      this.category=res;
      this.categoryForm.get('name').setValue(this.category.name);
    this.categoryForm.get('description').setValue(this.category.description);
    this.categoryForm.get('photo').setValue(this.category.photo);
    },
    err=>{
      this.logger.error("Error","Something bad happened.");
    });
  }
  }

  uploadFile(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.categoryForm.get('photo').setValue(file);
    }
  }
}
