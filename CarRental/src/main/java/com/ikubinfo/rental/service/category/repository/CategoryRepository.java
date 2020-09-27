package com.ikubinfo.rental.service.category.repository;

import com.ikubinfo.rental.service.category.dto.CategoryEntity;
import com.ikubinfo.rental.service.category.dto.CategoryFilter;

import java.util.List;

public interface CategoryRepository {

    CategoryEntity getByName(String name);

    CategoryEntity getById(Long id);

    List<CategoryEntity> getAll(CategoryFilter categoryFilter);

    List<CategoryEntity> getAll();

    Long countCategories();

    CategoryEntity save(CategoryEntity entity);

    void edit(CategoryEntity entity);

    void checkIfAnotherCategoryWithSameNameExists(String name, Long id);


}
