package com.ikubinfo.rental.service.statistics.dto;

import lombok.Data;

@Data
public class StatisticsModel {

    private Long availableCars;
    private Long rentedCars;
    private Long activeUsers;
    private Long newBookings;
}
