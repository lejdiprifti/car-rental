package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.CategoryAlreadyExistsException;
import com.ikubinfo.rental.exceptions.NonValidDataException;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.model.CategoryPage;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepository catRepository;
    @Autowired
    private CategoryConverter catConverter;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private CarRepository carRepository;

    public CategoryPage getAll(int startIndex, int pageSize) {
		LOGGER.info("Getting all the categories.");
        CategoryPage categoryPage = new CategoryPage();
        categoryPage.setTotalRecords(catRepository.countCategories());
        categoryPage.setCategoryList(catConverter.toModel(catRepository.getAll(startIndex, pageSize)));
        return categoryPage;
    }

    public List<CategoryModel> getAll() {
		LOGGER.info("Getting all the categories.");
        return catConverter.toModel(catRepository.getAll());

    }

    public CategoryModel getById(Long id) {
        try {
            return catConverter.toModel(catRepository.getById(id));
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
    }

    public CategoryModel save(CategoryModel model, MultipartFile file) {
        authorizationService.isUserAuthorized();
        try {
            validateCategoryData(model, file);
            saveIfAvailable(model.getName());
            CategoryEntity entity = catConverter.toEntity(model);
            if (file != null) {
                entity.setPhoto(file.getBytes());
            }
            entity.setActive(true);
            CategoryEntity categoryEntity = catRepository.save(entity);
            return catConverter.toModel(categoryEntity);
        } catch (NonValidDataException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (CategoryAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
        }
    }

    public void edit(CategoryModel model, MultipartFile file, Long id) {
        authorizationService.isUserAuthorized();
        try {
            validateCategoryData(model, file);
            CategoryEntity entity = catRepository.getById(id);
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
        } catch (NonValidDataException | IOException e) {
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

    private void saveIfAvailable(String name) throws CategoryAlreadyExistsException {
        try {
            catRepository.getByName(name);
            throw new CategoryAlreadyExistsException("Category already exists.");
        } catch (NoResultException e) {
            LOGGER.info("Category is available to be added.");
        }
    }

    private void updateIfAvailable(String name, Long id) throws CategoryAlreadyExistsException {
        try {
            catRepository.checkIfExistsAnother(name, id);
            throw new CategoryAlreadyExistsException("Category already exists.");
        } catch (NoResultException e) {
            LOGGER.info("Category is available to be updated.");
        }
    }

    public void checkIfCategoryExists(Long categoryId) {
    	LOGGER.debug("Checking if category with id {} exists", categoryId);
        try {
            catRepository.getById(categoryId);
        } catch (NoResultException e) {
            LOGGER.debug("Category with id {} does not exists.", categoryId);
            throw new CarRentalNotFoundException(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
        }
    }

    private void validateCategoryData(CategoryModel model, MultipartFile file) throws NonValidDataException {
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
