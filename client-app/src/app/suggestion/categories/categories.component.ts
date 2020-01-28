import { Component, OnInit } from '@angular/core';
import { CategoryService } from '@ikubinfo/core/services/category.service';
import { Category } from '@ikubinfo/core/models/category';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { Router } from '@angular/router';

@Component({
  selector: 'ikubinfo-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  categories: Array<Category>;
  constructor(private categoryService: CategoryService, private logger: LoggerService, private router: Router) { }

  ngOnInit() {
    this.categories = [];
    this.getAll();
  }

  getAll() {
    this.categoryService.getAll().subscribe(res => {
      this.categories = res;
    }, err => {
      this.logger.error('Error', 'Categories could not be found.');
    })
  }

  addCategory(){
    this.router.navigate(['/rental/category']);
  }
}
