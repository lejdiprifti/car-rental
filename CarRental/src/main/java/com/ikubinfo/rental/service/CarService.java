package com.ikubinfo.rental.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
		List<CarModel> modelList = carConverter.toModel(carRepository.getAll());
		for (CarModel car : modelList) {
			car.setReservedDates(getReservedDatesByCar(car.getId()));
		}
		return modelList;
	}
	
	public List<ReservedDates> getReservedDatesByCar(Long carId){
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
			checkIfExists(model.getPlate(), null);
			CarEntity entity = new CarEntity();
			entity.setAvailability(true);
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
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already exists.");
		}
		}
	
	public void edit(CarModel model,MultipartFile file, Long id) {
		authorizationService.isUserAuthorized();
		try {
			CarEntity entity = carRepository.getById(id);
			if (model.getPlate() != null) {
				checkIfExists(model.getPlate(), id);
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
			entity.setAvailability(model.isAvailability());
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
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already  exists.");
		}
		}
	
	public void delete(Long id) {
		authorizationService.isUserAuthorized();
		try {
			CarEntity entity = carRepository.getById(id);
			entity.setActive(false);
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found.");
		}
		}
	public void checkIfExists(String plate, Long id) throws Exception {
		try {
			if (id == null) {
			carRepository.getByPlate(plate);
			logger.error("Car already exists.");
			throw new Exception("Car already exists.");
			} else {
				carRepository.checkIfExistsAnother(plate, id);
				logger.error("Car already exists.");
				throw new Exception("Car already exists.");
			}
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}
}
