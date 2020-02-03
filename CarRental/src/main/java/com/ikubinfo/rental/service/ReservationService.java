package com.ikubinfo.rental.service;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.converter.ReservationConverter;
import com.ikubinfo.rental.entity.ReservationEntity;
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
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public ReservationService() {
		
	}
	
	public List<ReservationModel> getAll(){
		if ((int) jwtTokenUtil.getRole().get("id") == 1) {
			return reservationConverter.toModel(reservationRepository.getAll());
		} else {
			return reservationConverter.toModel(reservationRepository.getByUser(jwtTokenUtil.getUsername()));
		}
	}
	
	public ReservationModel getById(Long id) {
		try {
			return reservationConverter.toModel(reservationRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation was not found.");
		}
	}
	
	
	public void save(ReservationModel model) {
		if (model.getEndDate().after(model.getStartDate())) {
		if (reservationRepository.checkIfAvailable(model.getCarId(), model.getStartDate(), model.getEndDate()) == true) {
			ReservationEntity entity = new ReservationEntity();
			entity.setStartDate(model.getStartDate());
			entity.setEndDate(model.getEndDate());
			entity.setCreated_at(new GregorianCalendar().getTime());
			entity.setActive(true);
			entity.setCar(carRepository.getById(model.getCarId()));
			entity.setUser(userRepository.getByUsername(jwtTokenUtil.getUsername()));
			reservationRepository.save(entity);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen car is not available.");
		}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad dates are provided.");
		}
	}
	
	public void edit(ReservationModel model, Long id) {
		if (model.getEndDate().after(model.getStartDate())) {
		if (reservationRepository.checkIfAvailable(model.getCar().getId(), model.getStartDate(), model.getEndDate()) == true) {
			try {
				ReservationEntity entity = reservationRepository.getById(id);
				entity.setStartDate(model.getStartDate());
				entity.setEndDate(model.getEndDate());
				entity.setCreated_at(new GregorianCalendar().getTime());
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
