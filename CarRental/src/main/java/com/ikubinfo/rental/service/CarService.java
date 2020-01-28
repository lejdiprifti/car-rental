package com.ikubinfo.rental.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.converter.CarConverter;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.CategoryRepository;

@Service
public class CarService {
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private CarConverter carConverter;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private static Logger logger = LogManager.getLogger(CarService.class);
	
	public CarService() {
		
	}
	
	public List<CarModel> getAll() {
		return carConverter.toModel(carRepository.getAll());
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
	
	public void save(CarModel model) {
		try {
			checkIfExists(model.getPlate());
			CarEntity entity = new CarEntity();
			entity.setAvailability(true);
			entity.setActive(true);
			entity.setPhoto(model.getPhoto().getBytes());
			entity.setDescription(model.getDescription());
			entity.setDiesel(model.getDiesel());
			entity.setCategory(categoryRepository.getById(model.getCategoryId()));
			entity.setPrice(model.getPrice());
			entity.setType(model.getType());
			carRepository.save(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already exists.");
		}
	}
	
	public void edit(CarModel model, Long id) {
		try {
			CarEntity entity = carRepository.getById(id);
			if (model.getPlate() != null) {
				checkIfExists(model.getPlate());
				entity.setPlate(model.getPlate());
			}
			entity.setName(model.getName());
			entity.setPhoto(model.getPhoto().getBytes());
			entity.setPrice(model.getPrice());
			entity.setType(model.getType());
			entity.setDescription(model.getDescription());
			entity.setAvailability(model.isAvailability());
			entity.setDiesel(model.getDiesel());
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car/Category not found.");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already  exists.");
		}
	}
	
	public void delete(Long id) {
		try {
			CarEntity entity = carRepository.getById(id);
			entity.setActive(false);
			carRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found.");
		}
	}
	public void checkIfExists(String plate) throws Exception {
		try {
			carRepository.getByPlate(plate);
			logger.error("Car already exists.");
			throw new Exception("Car already exists.");
		} catch (NoResultException e) {
			logger.info("No car exists with the plate provided.");
		}
	}
}
