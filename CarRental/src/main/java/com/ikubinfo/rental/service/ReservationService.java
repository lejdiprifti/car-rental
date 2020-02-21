package com.ikubinfo.rental.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.converter.ReservationConverter;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.Mail;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.ReservationRepository;
import com.ikubinfo.rental.repository.UserRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationConverter reservationConverter;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	public ReservationService() {

	}

	public List<ReservationModel> getAll() {
		authorizationService.isUserAuthorized();
		return reservationConverter.toModelObject(reservationRepository.getAll());
	}

	public List<ReservationModel> getByUsername() {
		return reservationConverter.toModelObject(reservationRepository.getByUser(jwtTokenUtil.getUsername()));
	}

	public ReservationModel getById(Long id) {
		try {
			return reservationConverter.toModel(reservationRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
		}
	}

	public List<ReservationModel> getByCar(Long carId) {
		return reservationConverter.toModel(reservationRepository.getByCar(carId));
	}

	public void save(ReservationModel model) throws MessagingException, IOException {
		checkDates(model.getStartDate(), model.getEndDate());
		checkIfAvailable(model.getCarId(), model.getStartDate(), model.getEndDate());
		ReservationEntity entity = reservationConverter.toEntity(model);
		entity.setCreated_at(Calendar.getInstance());
		entity.setActive(true);
		entity.setUserId(userRepository.getByUsername(jwtTokenUtil.getUsername()).getId());
		reservationRepository.save(entity);
		Mail mail = emailService.setMailProperties(entity, model.getFee());
		CarEntity car = carRepository.getById(entity.getCarId());
		emailService.prepareAndSend(mail, car);
	}

	public void edit(ReservationModel model, Long id) {
		checkDates(model.getStartDate(), model.getEndDate());
		try {
			ReservationEntity entity = reservationRepository.getById(id);
			reservationRepository.updateIfAvailable(entity.getCarId(), model.getStartDate(), model.getEndDate(),
					entity.getId());
			entity.setCreated_at(Calendar.getInstance());
			entity.setStartDate(model.getStartDate());
			entity.setEndDate(model.getEndDate());
			reservationRepository.edit(entity);
			Mail mail = emailService.setMailProperties(entity, model.getFee());
			CarEntity car = carRepository.getById(entity.getCarId());
			emailService.prepareAndSend(mail, car);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
		} catch (MessagingException e) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
					"Sorry, we are not able to send you the e-mail.");
		}
	}

	public void cancel(Long id) {
		try {
			ReservationEntity entity = reservationRepository.getById(id);
			entity.setActive(false);
			reservationRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
		}
	}

	public void updateIfAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate, Long id) {
		Long reservations = reservationRepository.updateIfAvailable(carId, startDate, endDate, id);
		if (reservations > 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is reserved during the specified dates.");
		}
	}

	public void checkIfAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
		Long reservations = reservationRepository.checkIfAvailable(carId, startDate, endDate);
		if (reservations > 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is reserved during the specified dates.");
		}
	}

	public void checkDates(LocalDateTime startDate, LocalDateTime endDate) {
		if (!endDate.isAfter(startDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, specify a valid time period.");
		}
	}
	
	public int cancelByCarAndDate(LocalDateTime date, Long carId) {
		if (date.isAfter(LocalDateTime.now())) {
			return reservationRepository.cancelByCarAndDate(date, carId);
		} else {
			return reservationRepository.cancelByCar(carId);
		}
	}
}
