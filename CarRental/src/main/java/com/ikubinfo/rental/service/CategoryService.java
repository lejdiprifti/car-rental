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

import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository catRepository;

	@Autowired
	private CategoryConverter catConverter;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private CarRepository carRepository;

	private static Logger logger = LogManager.getLogger(CategoryService.class);

	public CategoryService() {

	}

	public List<CategoryModel> getAll() {
		logger.info("Getting all the categories.");
		return catConverter.toModel(catRepository.getAll());
	}

	public CategoryModel getById(Long id) {
		try {
			return catConverter.toModel(catRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		}
	}

	public void save(CategoryModel model, MultipartFile file) throws IOException {
		try {
			checkIfExists(model.getName(), null);
			CategoryEntity entity = new CategoryEntity();
			entity.setName(model.getName());
			if (file != null) {
				entity.setPhoto(file.getBytes());
			}
			entity.setActive(true);
			entity.setDescription(model.getDescription());
			catRepository.save(entity);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
		}
	}

	public void edit(CategoryModel model, MultipartFile file, Long id) throws IOException {
		authorizationService.isUserAuthorized();
			try {
				CategoryEntity entity = catRepository.getById(id);
				if (model.getName() != null) {
					try {
						checkIfExists(model.getName(), id);
						entity.setName(model.getName());
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
					}
				}
				if (model.getDescription() != null) {
					entity.setDescription(model.getDescription());
				}
				if (file != null) {
					entity.setPhoto(file.getBytes());
				}
				catRepository.edit(entity);
			} catch (NoResultException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
			}
		} 

	public void delete(Long id) {
		authorizationService.isUserAuthorized();
			if (carRepository.getByCategory(id).size() == 0) {
				try {
					CategoryEntity entity = catRepository.getById(id);
					entity.setActive(false);
					catRepository.edit(entity);
				} catch (NoResultException e) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
				}
			} else {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
						"You are not allowed to perform this action.");
			}
		}

	public void checkIfExists(String name, Long id) throws Exception {
		try {
			if (id == null) {
				catRepository.getByName(name);
				logger.error("Category already exists.");
				throw new Exception("Category already exists.");
			} else {
				// when you need to update the category, check if there is another category with
				// the same name not the one being updated.
				catRepository.checkIfExistsAnother(name, id);
				throw new Exception("Category already exists.");
			}
		} catch (NoResultException e) {

		}
	}

}
