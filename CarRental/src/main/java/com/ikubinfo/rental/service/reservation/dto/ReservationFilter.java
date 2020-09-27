package com.ikubinfo.rental.service.reservation.dto;

import com.ikubinfo.rental.service.filter.PaginationAndDateFilter;
import lombok.Data;

@Data
public class ReservationFilter extends PaginationAndDateFilter {
    private String startDate = "1900-01-01 00:00:00";
    private String endDate = "2900-01-01 00:00:00";
    private String carName = "";
}
