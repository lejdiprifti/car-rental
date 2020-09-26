package com.ikubinfo.rental.service.category.repository;

import com.ikubinfo.rental.service.category.dto.CategoryEntity;

import java.util.List;

public interface CategoryRepository {

    CategoryEntity getByName(String name);

    CategoryEntity getById(Long id);

    List<CategoryEntity> getAll(int startIndex, int pageSize);

    List<CategoryEntity> getAll();

    Long countCategories();

    CategoryEntity save(CategoryEntity entity);

    void edit(CategoryEntity entity);

    void checkIfAnotherCategoryWithSameNameExists(String name, Long id);


}
