package com.ikubinfo.rental.model;

import lombok.Data;

import java.util.List;

@Data
public class CategoryPage {

    private Long totalRecords;
    private List<CategoryModel> categoryList;

}
