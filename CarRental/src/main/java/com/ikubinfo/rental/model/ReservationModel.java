package com.ikubinfo.rental.model;

import java.util.Date;

public class ReservationModel {
	
	private Long id;
	private UserModel user;
	private CarModel car;
	private Date startDate;
	private Date endDate;
	private boolean active;
	
	public ReservationModel() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public CarModel getCar() {
		return car;
	}

	public void setCar(CarModel car) {
		this.car = car;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "ReservationModel [id=" + id + ", user=" + user + ", car=" + car + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", active=" + active + "]";
	}
	
	
}
