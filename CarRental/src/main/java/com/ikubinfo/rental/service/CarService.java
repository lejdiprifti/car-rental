package com.ikubinfo.rental.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.config.ActiveReservationsException;
import com.ikubinfo.rental.config.CarAlreadyExistsException;
import com.ikubinfo.rental.converter.CarConverter;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservedDates;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.CategoryRepository;
import com.ikubinfo.rental.repository.ReservationRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CarConverter carConverter;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private ReservationRepository reservationRepository;
	private static Logger logger = LogManager.getLogger(CarService.class);

	public CarService() {

	}

	public List<CarModel> getAll() {
		try {
			authorizationService.isUserAuthorized();
			List<CarModel> modelList = carConverter.toModel(carRepository.getAll());
			for (CarModel car : modelList) {
				car.setReservedDates(getReservedDatesByCar(car.getId()));
			}
			return modelList;
		} catch (ResponseStatusException e) {
			List<CarModel> modelList = carConverter.toModel(carRepository.getAllAvailable());
			for (CarModel car : modelList) {
				car.setReservedDates(getReservedDatesByCar(car.getId()));
			}
			return modelList;
		}
	}

	public List<ReservedDates> getReservedDatesByCar(Long carId) {
		return reservationRepository.getReservedDatesByCar(carId);
	}

	public CarModel getById(Long id) {
		try {
			return carConverter.toModel(carRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found.");
		}
	}

	public List<CarModel> getByCategory(Long categoryId) {
		try {
			return carConverter.toModel(carRepository.getByCategory(categoryId));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		}
	}

	public void save(CarModel model, MultipartFile file) {
		authorizationService.isUserAuthorized();
		try {
			saveIfAvailable(model.getPlate());
			CarEntity entity = new CarEntity();
			entity.setAvailability(model.getAvailability());
			entity.setActive(true);
			entity.setName(model.getName());
			entity.setPhoto(file.getBytes());
			entity.setDescription(model.getDescription());
			entity.setDiesel(model.getDiesel());
			entity.setPlate(model.getPlate());
			entity.setCategory(categoryRepository.getById(model.getCategoryId()));
			if (model.getPrice() > 0) {
				entity.setPrice(model.getPrice());
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price cannot be negative.");

			}
			if (model.getYear() > 0) {
				entity.setYear(model.getYear());
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Year cannot be null/negative.");
			}
			entity.setType(model.getType());
			carRepository.save(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		} catch (CarAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already exists.");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Something bad happened. Please, try again later.");
		}
	}

	public void edit(CarModel model, MultipartFile file, Long id) {
		authorizationService.isUserAuthorized();
		try {
			CarEntity entity = carRepository.getById(id);
			if (model.getPlate() != null) {
				updateIfAvailable(model.getPlate(), id);
				entity.setPlate(model.getPlate());
			}
			if (model.getName() != null) {
				entity.setName(model.getName());
			}
			if (file != null) {
				entity.setPhoto(file.getBytes());
			}
			if (model.getPrice() > 0) {
				entity.setPrice(model.getPrice());
			}
			if (model.getType() != null) {
				entity.setType(model.getType());
			}
			if (model.getDescription() != null) {
				entity.setDescription(model.getDescription());
			}
			if (model.getAvailability() != null) {
				entity.setAvailability(model.getAvailability());
			}
			if (model.getDiesel() != null) {
				entity.setDiesel(model.getDiesel());
			}
			if (model.getCategoryId() != null) {
				entity.setCategory(categoryRepository.getById(model.getCategoryId()));
			}
			if (model.getYear() > 0) {
				entity.setYear(model.getYear());
			}
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car/Category not found.");
		} catch (CarAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already  exists.");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Something bad happened. Please, try again later.");
		}
	}

	public void delete(Long id) {
		authorizationService.isUserAuthorized();
		try {
			hasActiveReservations(id);
			CarEntity entity = carRepository.getById(id);
			entity.setActive(false);
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found.");
		} catch (ActiveReservationsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Cannot delete car because it has active reservations.");
		}
	}

	public void saveIfAvailable(String plate) throws CarAlreadyExistsException {
		try {
			carRepository.getByPlate(plate);
			throw new CarAlreadyExistsException("Car already exists.");
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}

	public void updateIfAvailable(String plate, Long id) throws CarAlreadyExistsException {
		try {
			carRepository.checkIfExistsAnother(plate, id);
			throw new CarAlreadyExistsException("Car already exists.");
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}

	public void hasActiveReservations(Long carId) throws ActiveReservationsException {
		if (reservationRepository.countActiveReservationsByCar(carId) > 0) {
			throw new ActiveReservationsException("Car has still active reservations.");
		}
	}
}
