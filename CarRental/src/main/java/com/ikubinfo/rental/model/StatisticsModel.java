package com.ikubinfo.rental.model;

import lombok.Data;

@Data
public class StatisticsModel {

    private Long availableCars;
    private Long rentedCars;
    private Long activeUsers;
    private Long newBookings;
}
