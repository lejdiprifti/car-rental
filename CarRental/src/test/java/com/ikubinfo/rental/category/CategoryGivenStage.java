package com.ikubinfo.rental.category;

import com.ikubinfo.rental.car.CarGivenStage;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.CategoryService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CategoryGivenStage extends Stage<CategoryGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CarService carService;

    @ProvidedScenarioState
    private CategoryModel savedCategoryModel;

    public CategoryGivenStage user_is_logged_in_as_admin() {
        tokenCreator.createAdminToken();
        return self();
    }

    public CategoryGivenStage user_is_logged_in_as_user() {
        tokenCreator.createUserToken();
        return self();
    }

    public CategoryGivenStage admin_adds_new_category() {
        user_is_logged_in_as_admin();
        savedCategoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
        return self();
    }

    public CategoryGivenStage admin_has_added_two_categories() {
        admin_adds_new_category();
        CategoryModel categoryModel = createCategoryModel();
        categoryModel.setName("some different name");
        savedCategoryModel = categoryService.save(categoryModel, createMultipartFile());
        return self();
    }

    public CategoryGivenStage category_has_cars() {
        admin_adds_new_category();
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carModel.setCategoryId(savedCategoryModel.getId());
        carService.save(carModel, createMultipartFile());
        return self();
    }
}
