package com.ikubinfo.rental.model;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ReservationModel {
	
	private Long id;
	private UserModel user;
	private CarModel car;
	private Long carId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Date created_at;
	private double fee;
	private boolean active;	
}
