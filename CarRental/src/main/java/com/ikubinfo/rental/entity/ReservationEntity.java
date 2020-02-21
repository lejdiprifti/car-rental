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

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation", schema = "rental")
@Getter
@Setter
@NoArgsConstructor
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
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
