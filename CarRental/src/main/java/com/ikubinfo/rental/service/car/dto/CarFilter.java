package com.ikubinfo.rental.service.car.dto;

import com.ikubinfo.rental.service.filter.PaginationAndDateFilter;
import lombok.Data;

import java.util.List;

@Data
public class CarFilter extends PaginationAndDateFilter {
    private List<Long> selectedCategoryIds;
    private String startDate = "1900-01-01 00:00:00";
    private String endDate = "1900-01-01 00:00:00";
    private String brand = "";
}
