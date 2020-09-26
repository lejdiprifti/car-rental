package com.ikubinfo.rental.user;

import com.ikubinfo.rental.service.car.status.StatusEnum;
import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.category.dto.CategoryModel;
import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.car.CarServiceImpl;
import com.ikubinfo.rental.service.category.CategoryServiceImpl;
import com.ikubinfo.rental.service.reservation.ReservationServiceImpl;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.reservation.util.ReservationUtil.createReservationModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class UserGivenStage extends Stage<UserGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @ProvidedScenarioState
    private CarModel savedCarModel;

    @Autowired
    private ReservationServiceImpl reservationService;

    public UserGivenStage user_is_logged_in_as_user() {
        tokenCreator.createUserToken();
        return self();
    }

    public UserGivenStage user_reserves_a_car() {
        CategoryModel categoryModel = addCategory();
        addCar(categoryModel);
        user_is_logged_in_as_user();
        reserveCar();
        return self();
    }

    private void reserveCar() {
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setCarId(savedCarModel.getId());
        reservationService.save(reservationModel);
    }

    private void addCar(CategoryModel categoryModel) {
        tokenCreator.createAdminToken();
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carModel.setCategoryId(categoryModel.getId());
        savedCarModel = carService.save(carModel, createMultipartFile());
    }

    private CategoryModel addCategory() {
        tokenCreator.createAdminToken();
        return categoryService.save(createCategoryModel(), createMultipartFile());
    }

}
