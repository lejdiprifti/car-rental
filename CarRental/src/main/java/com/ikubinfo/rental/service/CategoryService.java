package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.service.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.model.page.CategoryPage;
import com.ikubinfo.rental.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryConverter categoryConverter;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private CarService carService;

    public CategoryPage getAll(int startIndex, int pageSize) {
        LOGGER.info("Getting all the categories from startIndex {} with pageSize {}", startIndex, pageSize);
        CategoryPage categoryPage = new CategoryPage();
        categoryPage.setTotalRecords(categoryRepository.countCategories());
        categoryPage.setCategoryList(categoryConverter.toModel(categoryRepository.getAll(startIndex, pageSize)));
        return categoryPage;
    }

    public List<CategoryModel> getAll() {
        LOGGER.info("Getting all the categories.");
        return categoryConverter.toModel(categoryRepository.getAll());
    }

    public CategoryModel getById(Long id) {
        try {
            return categoryConverter.toModel(categoryRepository.getById(id));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
        }
    }

    public CategoryModel save(CategoryModel model, MultipartFile file) {
        authorizationService.isUserAuthorized();
        validateCategoryData(model, file);
        checkIfSaveIsAvailable(model.getName());
        CategoryEntity categoryEntity = executeSaveCategory(model, file);
        return categoryConverter.toModel(categoryEntity);
    }

    private CategoryEntity executeSaveCategory(CategoryModel model, MultipartFile file) {
        try {
            CategoryEntity entity = categoryConverter.toEntity(model);
            if (file != null) {
                entity.setPhoto(file.getBytes());
            }
            entity.setActive(true);
            return categoryRepository.save(entity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException(e.getMessage());
        }
    }

    private void checkIfSaveIsAvailable(String name) {
        try {
            categoryRepository.getByName(name);
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.debug("Category is available to be added.");
        }
    }

    public void edit(CategoryModel model, MultipartFile file, Long id) {
        try {
            authorizationService.isUserAuthorized();
            validateCategoryData(model, file);
            checkIfCategoryExists(id);
            checkIfUpdateIsAvailable(model.getName(), id);
            CategoryEntity entity = categoryRepository.getById(id);
            entity.setDescription(model.getDescription());
            entity.setName(model.getName());
            if (file != null) {
                entity.setPhoto(file.getBytes());
            }
            categoryRepository.edit(entity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException(e.getMessage());
        }
    }

    private void checkIfUpdateIsAvailable(String name, Long id) {
        try {
            categoryRepository.checkIfAnotherCategoryWithSameNameExists(name, id);
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.info("Category is available to be updated.");
        }
    }

    public void delete(Long categoryId) {
        authorizationService.isUserAuthorized();
        checkIfCategoryExists(categoryId);
        checkIfCategoryCanBeDeleted(categoryId);
        executeDeleteCategory(categoryId);
    }

    private void executeDeleteCategory(Long categoryId) {
        CategoryEntity entity = categoryRepository.getById(categoryId);
        entity.setActive(false);
        categoryRepository.edit(entity);
    }

    private void checkIfCategoryCanBeDeleted(Long categoryId) {
        if (carService.getByCategory(categoryId).size() != 0) {
            throw new CarRentalBadRequestException(BadRequest.CATEGORY_CONTAINS_CARS.getErrorMessage());
        }
    }

    public void checkIfCategoryExists(Long categoryId) {
        try {
            LOGGER.info("Checking if category with id {} exists", categoryId);
            categoryRepository.getById(categoryId);
            LOGGER.info("Category with id {} exists.", categoryId);
        } catch (NoResultException e) {
            LOGGER.info("Category with id {} does not exists.", categoryId);
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
