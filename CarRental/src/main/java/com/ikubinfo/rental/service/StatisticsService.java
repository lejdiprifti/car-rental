package com.ikubinfo.rental.service;

import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.StatisticsModel;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.ReservationRepository;
import com.ikubinfo.rental.repository.UserRepository;

@Service
public class StatisticsService {
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	
	public StatisticsService() {
		
	}
	
	
	public StatisticsModel getStatistics() {
		authorizationService.isUserAuthorized();
		StatisticsModel stats = new StatisticsModel();
		stats.setActiveUsers(userRepository.countActiveUsers());
		stats.setAvailableCars(carRepository.countCars(StatusEnum.AVAILABLE));
		stats.setRentedCars(carRepository.countCars(StatusEnum.RENTED));
		
		Date today = new GregorianCalendar().getTime();
		today.setDate(new GregorianCalendar().getTime().getDate() - 2);
		stats.setNewBookings(reservationRepository.countNewBookings(today));
		return stats;
	}
}
