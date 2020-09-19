package com.ikubinfo.rental.category;

import com.ikubinfo.rental.car.CarGivenStage;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CategoryService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CategoryGivenStage extends Stage<CategoryGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CategoryService categoryService;

    @ProvidedScenarioState
    private CategoryModel savedCategoryModel;

    public CategoryGivenStage user_is_logged_in_as_admin() {
        tokenCreator.createAdminToken();
        return self();
    }

    public CategoryGivenStage admin_adds_new_category() {
        savedCategoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
        return self();
    }
}
