package com.ikubinfo.rental.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationPage {
	
	private Long totalRecords;
	private List<ReservationModel> reservationList;
}
