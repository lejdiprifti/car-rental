package com.ikubinfo.rental.model;

import java.util.Date;

public class ReservationModel {
	
	private Long id;
	private UserModel user;
	private CarModel car;
	private Long carId;
	private Date startDate;
	private Date endDate;
	private Date created_at;
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

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	@Override
	public String toString() {
		return "ReservationModel [id=" + id + ", user=" + user + ", car=" + car + ", carId=" + carId + ", startDate="
				+ startDate + ", endDate=" + endDate + ", created_at=" + created_at + ", active=" + active + "]";
	}



	
	
}
