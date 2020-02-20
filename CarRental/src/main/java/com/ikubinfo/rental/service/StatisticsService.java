package com.ikubinfo.rental.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		stats.setAvailableCars(carRepository.countFreeCars());
		stats.setRentedCars(carRepository.countRentedCars());
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		stats.setNewBookings(reservationRepository.countNewBookings(calendar));
		return stats;
	}
}
