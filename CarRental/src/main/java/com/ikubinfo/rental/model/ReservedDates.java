package com.ikubinfo.rental.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservedDates {
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
}
