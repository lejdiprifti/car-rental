package com.ikubinfo.rental.model.filter;

import lombok.Data;

import java.util.List;

@Data
public class CarFilter {
    private int startIndex = 0;
    private int pageSize = 10;
    private List<Long> selectedCategoryIds;
    private String startDate = "1900-01-01 00:00:00";
    private String endDate  = "1900-01-01 00:00:00";
    private String brand = "";
}
