package com.ikubinfo.rental.model;

import java.time.LocalDateTime;
import java.util.Date;

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

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "ReservationModel [id=" + id + ", user=" + user + ", car=" + car + ", carId=" + carId + ", startDate="
				+ startDate + ", endDate=" + endDate + ", created_at=" + created_at + ", fee=" + fee + ", active="
				+ active + "]";
	}

	



	
	
}
