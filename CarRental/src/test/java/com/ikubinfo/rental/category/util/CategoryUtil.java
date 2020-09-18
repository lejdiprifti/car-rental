package com.ikubinfo.rental.category.util;

import com.ikubinfo.rental.entity.CategoryEntity;

public class CategoryUtil {

    public static CategoryEntity createCategoryEntity() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setPhoto(new byte[0]);
        categoryEntity.setDescription("some description");
        categoryEntity.setName("some name");
        categoryEntity.setActive(true);
        return categoryEntity;
    }

    private CategoryUtil() {}
}
