package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.CategoryService;
import com.ikubinfo.rental.service.ReservationService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.reservation.util.ReservationUtil.createReservationModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class ReservationGivenStage extends Stage<ReservationGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CategoryService categoryService;

    @ProvidedScenarioState
    private CarModel savedCarModel;

    @ProvidedScenarioState
    private ReservationModel savedReservationModel;

    public ReservationGivenStage user_is_logged_in_as_user() {
        tokenCreator.createUserToken();
        return self();
    }

    public ReservationGivenStage admin_adds_an_available_car() {
        tokenCreator.createAdminToken();
        CategoryModel categoryModel = addCategory();
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carModel.setCategoryId(categoryModel.getId());
        savedCarModel = carService.save(carModel, createMultipartFile());
        return self();
    }

    public ReservationGivenStage user_reserves_car() {
        admin_adds_an_available_car();
        user_is_logged_in_as_user();
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setCarId(savedCarModel.getId());
        savedReservationModel = reservationService.save(reservationModel);
        return self();
    }

    private CategoryModel addCategory() {
        return categoryService.save(createCategoryModel(), createMultipartFile());
    }

    public ReservationGivenStage user_has_made_two_reservations() {
        user_reserves_car();
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setStartDate(LocalDateTime.now().plusDays(11));
        reservationModel.setEndDate(LocalDateTime.now().plusDays(12));
        reservationModel.setCarId(savedCarModel.getId());
        savedReservationModel = reservationService.save(reservationModel);
        return self();
    }

    public ReservationGivenStage user_is_logged_in_as_admin() {
        tokenCreator.createAdminToken();
        return self();
    }

}
