package com.ikubinfo.rental.service.category.dto;

import com.ikubinfo.rental.service.category.dto.CategoryModel;
import lombok.Data;

import java.util.List;

@Data
public class CategoryPage {

    private Long totalRecords;
    private List<CategoryModel> categoryList;

}
