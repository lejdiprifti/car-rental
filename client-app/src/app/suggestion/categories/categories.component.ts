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
  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private confirmationService: ConfirmationService,
    private logger: LoggerService
  ) {}

  ngOnInit() {
    this.categories = [];
    this.cols = cols;
    this.getAll();

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

  getAll() {
    this.categoryService.getAll().subscribe(
      res => {
        this.categories = res;
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
              addedCategory.photo,
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
                this.getAll();
              },
              err => {
                this.logger.error("Error", err.error.message);
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
              categories[this.categories.indexOf(this.selectedCategory)].photo,
              new Blob(
                [
                  JSON.stringify({
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
                  this.getAll();
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
        this.getAll();
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

  uploadFile(event) {
    if (event.target.files.length > 0 && event.target.files[0] < 50000) {
      const file = event.target.files[0];
      this.category.photo = file;
    } else {
      this.logger.warning("Warning!", "Photo must be less than 50 Kb.");
    }
  }
}
