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

import com.ikubinfo.rental.config.CategoryAlreadyExistsException;
import com.ikubinfo.rental.config.NonValidDataException;
import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.model.CategoryPage;
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

	public CategoryPage getAll(int startIndex, int pageSize) {
		logger.info("Getting all the categories.");
		CategoryPage categoryPage = new CategoryPage();
		categoryPage.setTotalRecords(catRepository.countCategories(startIndex, pageSize));
		categoryPage.setCategoryList(catConverter.toModel(catRepository.getAll(startIndex, pageSize)));
		return categoryPage;
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
			validateCategoryData(model, file);
			saveIfAvailable(model.getName());
			CategoryEntity entity = catConverter.toEntity(model);
			if (file != null) {
				entity.setPhoto(file.getBytes());
			}
			entity.setActive(true);
			catRepository.save(entity);
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CategoryAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
		}
	}

	public void edit(CategoryModel model, MultipartFile file, Long id) throws IOException {
		authorizationService.isUserAuthorized();
		try {
			validateCategoryData(model, file);
			CategoryEntity entity = catRepository.getById(model.getId());
			updateIfAvailable(model.getName(), id);
			entity.setDescription(model.getDescription());
			entity.setName(model.getName());
			if (file != null) {
				entity.setPhoto(file.getBytes());
			}
			catRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		} catch (CategoryAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
					"Category cannot be deleted as it has cars registered in it.");
		}
	}

	public void saveIfAvailable(String name) throws CategoryAlreadyExistsException {
		try {
			catRepository.getByName(name);
			throw new CategoryAlreadyExistsException("Category already exists.");
		} catch (NoResultException e) {
			logger.info("Category is available to be added.");
		}
	}

	public void updateIfAvailable(String name, Long id) throws CategoryAlreadyExistsException {
		try {
			catRepository.checkIfExistsAnother(name, id);
			throw new CategoryAlreadyExistsException("Category already exists.");
		} catch (NoResultException e) {
			logger.info("Category is available to be updated.");
		}
	}

	public void validateCategoryData(CategoryModel model, MultipartFile file) throws NonValidDataException {
		try {
			if (model.getName().trim() == "") {
				throw new NonValidDataException("Name is required.");
			}
			if (model.getDescription().trim() == "") {
				throw new NonValidDataException("Description is required.");
			}
			if (file == null) {
				if (model.getId() == null) {
					throw new NonValidDataException("Photo is required.");
				}
			}
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data are missing.");
		}
	}
}
