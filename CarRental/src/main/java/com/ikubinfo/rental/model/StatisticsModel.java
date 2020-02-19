package com.ikubinfo.rental.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsModel {
	
	private Long availableCars;
	private Long rentedCars;
	private Long activeUsers;
	private Long newBookings;
}
