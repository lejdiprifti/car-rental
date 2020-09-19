package com.ikubinfo.rental.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.exceptions.ActiveReservationsException;
import com.ikubinfo.rental.exceptions.CarAlreadyExistsException;
import com.ikubinfo.rental.exceptions.NonValidDataException;
import com.ikubinfo.rental.converter.CarConverter;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CarsPage;
import com.ikubinfo.rental.model.ReservedDates;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.ReservationRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CarConverter carConverter;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private ReservationRepository reservationRepository;
	private static Logger logger = LogManager.getLogger(CarService.class);

	public CarsPage getAllCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
			String endDate, String brand) {
		LocalDateTime startDate2 = getFilterData(startDate, endDate).get("startDate");
		LocalDateTime endDate2 = getFilterData(startDate, endDate).get("endDate");
		if (brand == null) {
			brand = "";
		}
		CarsPage carPage = new CarsPage();
		try {
			authorizationService.isUserAuthorized();
			carPage.setCarsList(getAll(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
			carPage.setTotalRecords(carRepository.countAllCars(selectedCategoryIds, startDate2, endDate2, brand));

		} catch (ResponseStatusException e) {
			carPage.setCarsList(
					getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
			carPage.setTotalRecords(carRepository.countAvailableCars(selectedCategoryIds, startDate2, endDate2, brand));
		}
		return carPage;
	}

	public List<CarModel> getAll(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate,
			LocalDateTime endDate, String brand) {
		List<CarModel> modelList = carConverter.toModelObject(
				carRepository.getAll(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
		for (CarModel car : modelList) {
			car.setReservedDates(getReservedDatesByCar(car.getId()));
		}
		return modelList;
	}

	public List<CarModel> getAllAvailable(int startIndex, int pageSize, List<Long> selectedCategoryIds,
			LocalDateTime startDate, LocalDateTime endDate, String brand) {
		List<CarModel> modelList = carConverter.toModelObject(
				carRepository.getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
		for (CarModel car : modelList) {
			car.setReservedDates(getReservedDatesByCar(car.getId()));
		}
		return modelList;
	}

	public HashMap<String, LocalDateTime> getFilterData(String startDate, String endDate) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		HashMap<String, LocalDateTime> dataMap = new HashMap<String, LocalDateTime>();
		LocalDateTime startDate2;
		LocalDateTime endDate2;
		if (startDate != null) {
			startDate2 = LocalDateTime.parse(startDate, dateFormatter);
		} else {
			startDate2 = LocalDateTime.parse("1900-01-01 00:00:00", dateFormatter);
		}
		dataMap.put("startDate", startDate2);
		if (endDate != null) {
			endDate2 = LocalDateTime.parse(endDate, dateFormatter);
		} else {
			endDate2 = LocalDateTime.parse("1900-01-01 00:00:00", dateFormatter);
		}
		dataMap.put("endDate", endDate2);
		return dataMap;
	}

	public List<ReservedDates> getReservedDatesByCar(Long carId) {
		return reservationRepository.getReservedDatesByCar(carId);
	}

	public CarModel getById(Long id) {
		try {
			CarModel car = carConverter.toModel(carRepository.getById(id));
			car.setReservedDates(getReservedDatesByCar(id));
			return car;
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

	public CarModel save(CarModel model, MultipartFile file) {
		authorizationService.isUserAuthorized();
		try {
			validateCarData(model, file);
			saveIfAvailable(model.getPlate());
			CarEntity entity = carConverter.toEntity(model);
			entity.setPhoto(file.getBytes());
			entity.setActive(true);
			CarEntity carEntity = carRepository.save(entity);
			return carConverter.toModel(carEntity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		} catch (CarAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already exists.");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Something bad happened. Please, try again later.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void edit(CarModel model, MultipartFile file, Long id) {
		authorizationService.isUserAuthorized();
		try {
			validateCarData(model, file);
			updateIfAvailable(model.getPlate(), id);
			CarEntity entity = carConverter.toEntity(model);
			if (file != null) {
				entity.setPhoto(file.getBytes());
			} else {
				entity.setPhoto(carRepository.getById(id).getPhoto());
			}
			entity.setActive(true);
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car/Category not found.");
		} catch (CarAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already  exists.");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Something bad happened. Please, try again later.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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

	private void saveIfAvailable(String plate) throws CarAlreadyExistsException {
		try {
			carRepository.getByPlate(plate);
			throw new CarAlreadyExistsException("Car already exists.");
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}

	private void updateIfAvailable(String plate, Long id) throws CarAlreadyExistsException {
		try {
			carRepository.checkIfExistsAnother(plate, id);
			throw new CarAlreadyExistsException("Car already exists.");
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}

	private void hasActiveReservations(Long carId) throws ActiveReservationsException {
		if (reservationRepository.countActiveReservationsByCar(carId) > 0) {
			throw new ActiveReservationsException("Car has still active reservations.");
		}
	}

	private void validateCarData(CarModel model, MultipartFile file) throws NonValidDataException {
		try {
			if (model.getName().trim() == "") {
				throw new NonValidDataException("Name is required.");
			}
			if (model.getDescription().trim() == "" || model.getDescription().length() > 10000) {
				throw new NonValidDataException("Description is required.");
			}
			if (model.getAvailability() == null) {
				throw new NonValidDataException("Availability is required.");
			}
			if (model.getDiesel().trim() == "") {
				throw new NonValidDataException("Diesel is required.");
			}
			if (model.getPlate().trim() == "") {
				throw new NonValidDataException("Plate is required.");
			}
			if (model.getPrice() < 0) {
				throw new NonValidDataException("Price must be positive.");
			}
			if (model.getYear() < 1769) {
				throw new NonValidDataException("Year must contain a valid value.");
			}
			if (file == null) {
				if (model.getId() == null) {
					throw new NonValidDataException("Photo is required.");
				}
			} else if (file.getSize() > 100000) {
				throw new NonValidDataException("Photo needs to be less than 100Kb.");
			}
			if (model.getType() == "") {
				throw new NonValidDataException("Brand is required.");
			}
			if (!model.getAvailability().equals(StatusEnum.AVAILABLE)
					&& !model.getAvailability().equals(StatusEnum.SERVIS)) {
				throw new NonValidDataException("Availability is required.");
			}
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data are missing.");
		}
	}
}
