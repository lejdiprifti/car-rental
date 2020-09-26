package com.ikubinfo.rental.category;

import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.service.category.dto.CategoryModel;
import com.ikubinfo.rental.service.category.CategoryServiceImpl;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CategoryWhenStage extends Stage<CategoryWhenStage> {

    @Autowired
    private CategoryServiceImpl categoryService;

    @ScenarioState
    private CategoryModel savedCategoryModel;

    @ProvidedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ProvidedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public CategoryWhenStage admin_tries_to_add_new_category() {
        try {
            savedCategoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CategoryWhenStage admin_tries_to_update_category() {
        try {
            CategoryModel categoryModel = createCategoryModel();
            categoryModel.setId(savedCategoryModel.getId());
            categoryModel.setDescription("some updated description");
            categoryService.edit(categoryModel, createMultipartFile(), savedCategoryModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public void admin_tries_to_delete_category() {
        try {
            categoryService.delete(savedCategoryModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
    }

    public void admin_tries_to_add_new_category_with_missing_data() {
        try {
            CategoryModel categoryModel = createCategoryModel();
            categoryModel.setDescription(null);
            categoryService.save(categoryModel, createMultipartFile());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
    }

    public CategoryWhenStage admin_tries_to_update_category_with_id_$(Long categoryId) {
        try {
            categoryService.edit(createCategoryModel(), createMultipartFile(), categoryId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }

    public CategoryWhenStage admin_tries_to_delete_non_existing_category_with_id_$(Long categoryId) {
        try {
            categoryService.delete(categoryId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }

    public CategoryWhenStage admin_tries_to_get_category_by_id(Long categoryId) {
        try {
            categoryService.getById(categoryId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }
}
