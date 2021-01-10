package com.ikubinfo.rental.service.reservation.dto;

import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ikubinfo.rental.service.user.dto.UserEntity;
import com.ikubinfo.rental.service.car.dto.CarEntity;
import lombok.Data;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

@Entity
@Table(name = "reservation", schema = "rental")
@Data
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private UserEntity user;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne
	@JoinColumnsOrFormulas({
			@JoinColumnOrFormula(column = @JoinColumn(name = "car_id", referencedColumnName = "car_id", insertable = false, updatable = false)) })
	private CarEntity car;

	@Column(name = "car_id")
	private Long carId;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_at;

	private boolean active;

}
