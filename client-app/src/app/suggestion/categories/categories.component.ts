import { Component, OnInit } from "@angular/core";
import { CategoryService } from "@ikubinfo/core/services/category.service";
import { Category } from "@ikubinfo/core/models/category";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { ConfirmationService } from "primeng/primeng";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

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
    this.cols = [
      { field: "photo", header: "Photo" },
      { field: "name", header: "Name" },
      { field: "description", header: "Description" }
    ];
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
            let formData = new FormData();
            formData.append("file", addedCategory.photo);
            formData.append(
              "properties",
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
                this.logger.error("Error", "Category name already exists.");
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
            let formData = new FormData();
            formData.append(
              "file",
              categories[this.categories.indexOf(this.selectedCategory)].photo
            );
            formData.append(
              "properties",
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
                  this.logger.error("Error", "Category name exists.");
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
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.category.photo = file;
    }
  }
}
