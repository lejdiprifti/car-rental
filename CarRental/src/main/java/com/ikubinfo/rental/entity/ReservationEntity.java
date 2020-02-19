package com.ikubinfo.rental.entity;

import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "reservation", schema = "rental")
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",referencedColumnName="user_id", insertable=false, updatable=false)
	private UserEntity user;
	
	@Column(name="user_id")
	private Long userId;
	
	@ManyToOne
	@JoinColumn(name = "car_id", referencedColumnName="car_id",insertable=false, updatable=false)
	private CarEntity car;
	
	@Column(name="car_id")
	private Long carId;
	
	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_at;

	private boolean active;

	public ReservationEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public CarEntity getCar() {
		return car;
	}

	public void setCar(CarEntity car) {
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

	public Calendar getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Calendar created_at) {
		this.created_at = created_at;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ReservationEntity [id=" + id + ", user=" + user + ", userId=" + userId + ", car=" + car + ", carId="
				+ carId + ", startDate=" + startDate + ", endDate=" + endDate + ", created_at=" + created_at
				+ ", active=" + active + "]";
	}

	

}
