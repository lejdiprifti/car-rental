package com.ikubinfo.rental.service.statistics;

import java.util.Calendar;

import com.ikubinfo.rental.service.authorization.AuthorizationService;
import com.ikubinfo.rental.service.authorization.AuthorizationServiceImpl;
import com.ikubinfo.rental.service.car.repository.CarRepository;
import com.ikubinfo.rental.service.reservation.repository.ReservationRepository;
import com.ikubinfo.rental.service.statistics.dto.StatisticsModel;
import com.ikubinfo.rental.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.rental.database.CarRepositoryImpl;
import com.ikubinfo.rental.database.ReservationRepositoryImpl;
import com.ikubinfo.rental.database.UserRepositoryImpl;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorizationService authorizationService;

	@Override
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
