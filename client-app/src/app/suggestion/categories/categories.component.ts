import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { CategoryService } from "@ikubinfo/core/services/category.service";
import { Category } from "@ikubinfo/core/models/category";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { cols } from "@ikubinfo/suggestion/categories/categories.constants";
import { prepareFormData } from "@ikubinfo/suggestion/util";

import { ConfirmationService } from "primeng/primeng";

@Component({
  selector: "ikubinfo-categories",
  templateUrl: "./categories.component.html",
  styleUrls: ["./categories.component.css"]
})
export class CategoriesComponent implements OnInit {
  categories: Array<Category>;
  category: Category = {};
  displayDialog: boolean;
  selectedCategory: Category;
  newCategory: boolean;
  cols: any[];
  categoryForm: FormGroup;
  totalRecords: number;
  first: number;
  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private confirmationService: ConfirmationService,
    private logger: LoggerService
  ) {}

  ngOnInit() {
    this.categories = [];
    this.cols = cols;
    this.first = 0;
    this.getAll(0,5);

    this.categoryForm = this.fb.group({
      name: ["", Validators.required],
      description: [
        "",
        [
          Validators.required,
          Validators.minLength(50),
          Validators.maxLength(255)
        ]
      ]
    });
  }

  getAll(startIndex: number, pageSize: number) {
    this.categoryService.getAll(startIndex,pageSize).subscribe(
      res => {
        this.categories = res.categoryList;
        this.totalRecords = res.totalRecords;
      },
      err => {
        this.logger.error("Error", "Categories could not be found.");
      }
    );
  }
  showDialogToAdd(): void {
    this.newCategory = true;
    this.category = {};
    this.displayDialog = true;
  }

  save() {
    let categories = [...this.categories];
    if (
      this.category.name &&
      this.category.description &&
      this.category.photo
    ) {
      if (this.newCategory) {
        let addedCategory = this.category;
        this.confirmationService.confirm({
          message: "Are you sure you want to add this category?",
          header: "Accept Confirmation",
          icon: "pi pi-info-circle",
          accept: () => {
            let formData = prepareFormData(
              addedCategory.photo || null,
              new Blob(
                [
                  JSON.stringify({
                    name: addedCategory.name,
                    description: addedCategory.description
                  })
                ],
                {
                  type: "application/json"
                }
              )
            );
            return this.categoryService.save(formData).subscribe(
              res => {
                this.logger.success(
                  "Success",
                  "Category was successfully created."
                );
                this.getAll(0,5);
              },
              err => {
                this.logger.log('Error', err.error.message);
                this.logger.error("Error", "Category could not be added.");
              }
            );
          }
        });
      } else {
        categories[
          this.categories.indexOf(this.selectedCategory)
        ] = this.category;
        this.confirmationService.confirm({
          message: "Are you sure you want to save the changes?",
          header: "Accept Confirmation",
          icon: "pi pi-info-circle",
          accept: () => {
            let formData = prepareFormData(
              categories[this.categories.indexOf(this.selectedCategory)].photo || null,
              new Blob(
                [
                  JSON.stringify({
                    id: categories[this.categories.indexOf(this.selectedCategory)].id,
                    name:
                      categories[this.categories.indexOf(this.selectedCategory)]
                        .name,
                    description:
                      categories[this.categories.indexOf(this.selectedCategory)]
                        .description
                  })
                ],
                {
                  type: "application/json"
                }
              )
            );
            this.categoryService
              .edit(
                formData,
                categories[this.categories.indexOf(this.selectedCategory)].id
              )
              .subscribe(
                res => {
                  this.logger.success("Success", "Data saved successfully!");
                  this.getAll(0,5);
                },
                err => {
                  this.logger.error("Error", err.error.message);
                }
              );
          }
        });
      }
      this.category = null;
      this.displayDialog = false;
    } else {
      this.logger.warning(
        "Warning!",
        "Please make sure all fields are filled."
      );
    }
  }

  onRowSelect(event) {
    this.newCategory = false;
    this.category = this.cloneCategory(event.data);
    this.displayDialog = true;
  }

  delete(): void {
    this.categoryService.delete(this.selectedCategory.id).subscribe(
      res => {
        this.logger.success("Success", "Category was deleted successfully!");
        this.category = null;
        this.displayDialog = false;
        this.getAll(0,5);
      },
      err => {
        this.logger.error("Error", err.error.message);
      }
    );
  }

  cloneCategory(c: Category): Category {
    let category = {};
    for (let prop in c) {
      category[prop] = c[prop];
    }
    return category;
  }

  uploadFile(event): void{
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.category.photo = file;
    }
  }

  paginate(event): void {
    this.first = event.first;
    this.getAll(this.first , 5);
  }
}
