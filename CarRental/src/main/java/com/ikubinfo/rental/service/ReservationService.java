package com.ikubinfo.rental.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.ReservationRepository;
import com.ikubinfo.rental.repository.UserRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;

import freemarker.template.TemplateException;

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
	
	public List<ReservationModel> getAll(){
			authorizationService.isUserAuthorized();
			return reservationConverter.toModel(reservationRepository.getAll());
	}
	
	public List<ReservationModel> getByUsername(){
		return reservationConverter.toModel(reservationRepository.getByUser(jwtTokenUtil.getUsername()));
	}
	
	public ReservationModel getById(Long id) {
		try {
			return reservationConverter.toModel(reservationRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
		}
	}
	
	public List<ReservationModel> getByCar(Long carId){
			return reservationConverter.toModel(reservationRepository.getByCar(carId));	
	}
	
	
	public void save(ReservationModel model) throws MessagingException, IOException, TemplateException {
		if (model.getEndDate().isAfter(model.getStartDate())) {
		if (reservationRepository.checkIfAvailable(model.getCarId(), model.getStartDate(), model.getEndDate()) == true) {
			ReservationEntity entity = new ReservationEntity();
			entity.setStartDate(model.getStartDate());
			entity.setEndDate(model.getEndDate());
			entity.setCreated_at(Calendar.getInstance());
			entity.setActive(true);
			CarEntity car = carRepository.getById(model.getCarId());
			entity.setCar(car);
			entity.setUser(userRepository.getByUsername(jwtTokenUtil.getUsername()));
			reservationRepository.save(entity);
			emailService.sendEmailTo(entity, model.getFee());
			car.setAvailability(StatusEnum.RENTED);
			carRepository.edit(car);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen car is not available.");
		}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad dates are provided.");
		}
	}
	
	public void edit(ReservationModel model, Long id) {
		if (model.getEndDate().isAfter(model.getStartDate())) {
			ReservationEntity entity = reservationRepository.getById(id);
		if (reservationRepository.checkIfAvailable(entity.getCar().getId(), model.getStartDate(), model.getEndDate()) == true) {
			try {
				entity.setStartDate(model.getStartDate());
				entity.setEndDate(model.getEndDate());
				entity.setCreated_at(Calendar.getInstance());
				reservationRepository.edit(entity);
			} catch (NoResultException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen car is not available.");
		}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad dates are provided.");
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
}
