package com.ikubinfo.rental.model;

import java.time.LocalDateTime;


public class ReservedDates {
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	public ReservedDates() {
		
	}

	
	public LocalDateTime getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}


	public LocalDateTime getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}


	@Override
	public String toString() {
		return "ReservedDates [startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	
}
