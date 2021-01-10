package com.ikubinfo.rental.category.util;

import com.ikubinfo.rental.service.category.dto.CategoryModel;

public class CategoryUtil {

    public static CategoryModel createCategoryModel() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setPhoto(new byte[0]);
        categoryModel.setDescription("some description");
        categoryModel.setName("some name");
        categoryModel.setActive(true);
        return categoryModel;
    }

    private CategoryUtil() {}
}
