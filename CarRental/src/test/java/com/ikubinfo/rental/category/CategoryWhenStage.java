package com.ikubinfo.rental.category;

import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CategoryService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CategoryWhenStage extends Stage<CategoryWhenStage> {

    @Autowired
    private CategoryService categoryService;

    @ScenarioState
    private CategoryModel savedCategoryModel;

    public CategoryWhenStage admin_tries_to_add_new_category() {
        savedCategoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
        return self();
    }

    public CategoryWhenStage admin_tries_to_update_category() {
        CategoryModel categoryModel = createCategoryModel();
        categoryModel.setId(savedCategoryModel.getId());
        categoryModel.setDescription("some updated description");
        categoryService.edit(categoryModel, createMultipartFile(), savedCategoryModel.getId());
        return self();
    }

    public CategoryWhenStage admin_tries_to_delete_category() {
        categoryService.delete(savedCategoryModel.getId());
        return self();
    }
}
