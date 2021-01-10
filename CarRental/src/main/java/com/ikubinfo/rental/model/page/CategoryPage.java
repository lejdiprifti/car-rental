package com.ikubinfo.rental.model.page;

import com.ikubinfo.rental.model.CategoryModel;
import lombok.Data;

import java.util.List;

@Data
public class CategoryPage {

    private Long totalRecords;
    private List<CategoryModel> categoryList;

}
