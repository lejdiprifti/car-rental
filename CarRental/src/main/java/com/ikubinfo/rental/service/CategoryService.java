package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.CategoryAlreadyExistsException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
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
        LOGGER.debug("Getting all the categories.");
        CategoryPage categoryPage = new CategoryPage();
        categoryPage.setTotalRecords(catRepository.countCategories());
        categoryPage.setCategoryList(catConverter.toModel(catRepository.getAll(startIndex, pageSize)));
        return categoryPage;
    }

    public List<CategoryModel> getAll() {
        LOGGER.debug("Getting all the categories.");
        return catConverter.toModel(catRepository.getAll());

    }

    public CategoryModel getById(Long id) {
        try {
            return catConverter.toModel(catRepository.getById(id));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
        }
    }

    public CategoryModel save(CategoryModel model, MultipartFile file) {
        authorizationService.isUserAuthorized();
        try {
            validateCategoryData(model, file);
            checkIfSaveIsAvailable(model.getName());
            CategoryEntity entity = catConverter.toEntity(model);
            if (file != null) {
                entity.setPhoto(file.getBytes());
            }
            entity.setActive(true);
            CategoryEntity categoryEntity = catRepository.save(entity);
            return catConverter.toModel(categoryEntity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException(e.getMessage());
        }
    }

    private void checkIfSaveIsAvailable(String name) throws CategoryAlreadyExistsException {
        try {
            catRepository.getByName(name);
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.debug("Category is available to be added.");
        }
    }

    public void edit(CategoryModel model, MultipartFile file, Long id) {
        authorizationService.isUserAuthorized();
        try {
            validateCategoryData(model, file);
            checkIfCategoryExists(id);
            checkIfUpdateIsAvailable(model.getName(), id);
            CategoryEntity entity = catRepository.getById(id);
            entity.setDescription(model.getDescription());
            entity.setName(model.getName());
            if (file != null) {
                entity.setPhoto(file.getBytes());
            }
            catRepository.edit(entity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException(e.getMessage());
        }
    }

    private void checkIfUpdateIsAvailable(String name, Long id) throws CategoryAlreadyExistsException {
        try {
            catRepository.checkIfExistsAnother(name, id);
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.info("Category is available to be updated.");
        }
    }

    public void delete(Long id) {
        authorizationService.isUserAuthorized();
        checkIfCategoryExists(id);
        checkIfCategoryCanBeDeleted(id);
        CategoryEntity entity = catRepository.getById(id);
        entity.setActive(false);
        catRepository.edit(entity);
    }

    private void checkIfCategoryCanBeDeleted(Long categoryId) {
        if (carRepository.getByCategory(categoryId).size() != 0) {
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_CONTAINS_CARS.getErrorMessage());
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

    private void validateCategoryData(CategoryModel model, MultipartFile file) {
        try {
            if (model.getName().trim().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.NAME_REQUIRED.getErrorMessage());
            }
            if (model.getDescription().trim().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.DESCRIPTION_REQUIRED.getErrorMessage());
            }
            if (file == null) {
                if (model.getId() == null) {
                    throw new CarRentalBadRequestException(BadRequest.PHOTO_REQUIRED.getErrorMessage());
                }
            }
        } catch (NullPointerException e) {
            throw new CarRentalBadRequestException(BadRequest.USER_DATA_MISSING.getErrorMessage());
        }
    }
}
