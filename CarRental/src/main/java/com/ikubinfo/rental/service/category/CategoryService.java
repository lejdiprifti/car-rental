package com.ikubinfo.rental.service.category;

import com.ikubinfo.rental.service.category.dto.CategoryModel;
import com.ikubinfo.rental.service.category.dto.CategoryPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryPage getAll(int startIndex, int pageSize);

    List<CategoryModel> getAll();

    CategoryModel getById(Long id);

    CategoryModel save(CategoryModel model, MultipartFile file);

    void edit(CategoryModel model, MultipartFile file, Long id);

    void delete(Long categoryId);

    void checkIfCategoryExists(Long categoryId);
}
