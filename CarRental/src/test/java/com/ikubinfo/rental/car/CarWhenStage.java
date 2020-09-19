package com.ikubinfo.rental.car;

import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.CategoryService;
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

    @ProvidedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ProvidedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public CarWhenStage admin_tries_to_add_new_car() {
        try {
            CategoryModel categoryModel = categoryService.save(createCategoryModel(), createMultipartFile());
            CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
            carModel.setCategoryId(categoryModel.getId());
            carService.save(carModel, createMultipartFile());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public CarWhenStage admin_tries_to_update_car() {
        CarModel toUpdateCarModel = createCarModelWithStatus(StatusEnum.SERVIS);
        carService.edit(toUpdateCarModel, createMultipartFile(), savedCarModel.getId());
        return self();
    }

    public CarWhenStage admin_tries_to_delete_car() {
        carService.delete(savedCarModel.getId());
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
}
