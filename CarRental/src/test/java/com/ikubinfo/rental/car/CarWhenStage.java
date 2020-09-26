package com.ikubinfo.rental.car;

import com.ikubinfo.rental.service.car.CarService;
import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.car.status.StatusEnum;
import com.ikubinfo.rental.service.category.CategoryService;
import com.ikubinfo.rental.service.category.dto.CategoryModel;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CarWhenStage extends Stage<CarWhenStage> {

    @Autowired
    private CarService carService;

    @Autowired
    private CategoryService categoryService;

    @ExpectedScenarioState
    private CarModel savedCarModel;

    @ExpectedScenarioState
    private CategoryModel savedCategoryModel;

    @ProvidedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ProvidedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public CarWhenStage admin_tries_to_add_new_car() {
        try {
            CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
            carModel.setCategoryId(savedCategoryModel.getId());
            carService.save(carModel, createMultipartFile());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_update_car() {
        try {
            CarModel toUpdateCarModel = createCarModelWithStatus(StatusEnum.SERVIS);
            toUpdateCarModel.setCategoryId(savedCategoryModel.getId());
            carService.edit(toUpdateCarModel, createMultipartFile(), savedCarModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_delete_car() {
        try {
            carService.delete(savedCarModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_add_new_car_with_missing_data() {
        try {
            CategoryModel categoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
            CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
            carModel.setCategoryId(categoryModel.getId());
            carModel.setAvailability(null);
            carService.save(carModel, createMultipartFile());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_get_car_by_id_$(Long carId) {
        try {
            carService.getById(carId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_add_car_with_non_existing_category(Long categoryId) {
        try {
            CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
            carModel.setCategoryId(categoryId);
            carService.save(carModel, createMultipartFile());
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_update_non_existing_car_with_id_$(Long carId) {
        try {
            CarModel toUpdateCarModel = createCarModelWithStatus(StatusEnum.SERVIS);
            toUpdateCarModel.setId(carId);
            toUpdateCarModel.setCategoryId(savedCategoryModel.getId());
            carService.edit(toUpdateCarModel, createMultipartFile(), carId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_delete_car_with_id_$(Long carId) {
        try {
            carService.delete(carId);
        } catch (CarRentalNotFoundException exception) {
            carRentalNotFoundException = exception;
        }
        return self();
    }
}
