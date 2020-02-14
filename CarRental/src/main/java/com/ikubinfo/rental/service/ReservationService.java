package com.ikubinfo.rental.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public List<ReservationModel> getAll(){
			authorizationService.isUserAuthorized();
			return reservationConverter.toModelObject(reservationRepository.getAll());
	}
	
	public List<ReservationModel> getByUsername(){
		return reservationConverter.toModelObject(reservationRepository.getByUser(jwtTokenUtil.getUsername()));
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
	
	
	public void save(ReservationModel model) throws MessagingException, IOException {
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
			Mail mail = setMailProperties(entity, model.getFee());
			emailService.prepareAndSend(mail, entity.getCar().getPhoto());
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
		if (reservationRepository.updateIfAvailable(entity.getCar().getId(), model.getStartDate(), model.getEndDate(), entity.getId()) == true) {
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
	
	
	public Mail setMailProperties(ReservationEntity reservation, double fee) {
		Mail mail = new Mail();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		mail.setFrom("ikubinfo.car.rentals@gmail.com");
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("name", reservation.getUser().getFirstName() + ' ' + reservation.getUser().getLastName());
		content.put("carName", reservation.getCar().getName());
		content.put("carBrand", reservation.getCar().getType());
		content.put("carPlate", reservation.getCar().getPlate());
		content.put("price", fee);
		content.put("startDate", reservation.getStartDate().format(formatter));
		content.put("endDate", reservation.getEndDate().format(formatter));
		content.put("signature", "Car Rentals Albania");
		content.put("location", "Papa Gjon Pali 3rd St. , Tirana, Albania");
		mail.setContent(content);
		mail.setTo(reservation.getUser().getEmail());
		mail.setSubject("Receipt Confirmation");
		return mail;
	}
}
