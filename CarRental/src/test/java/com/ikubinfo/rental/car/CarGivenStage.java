package com.ikubinfo.rental.car;

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

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.category.util.CategoryUtil.createCategoryModel;
import static com.ikubinfo.rental.reservation.util.ReservationUtil.createReservationModel;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CarGivenStage extends Stage<CarGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CarService carService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReservationService reservationService;

    @ProvidedScenarioState
    private CarModel savedCarModel;

    @ProvidedScenarioState
    private CategoryModel savedCategoryModel;

    public CarGivenStage user_is_logged_in_as_admin() {
        tokenCreator.createAdminToken();
        return self();
    }

    public CarGivenStage a_category_is_added() {
        user_is_logged_in_as_admin();
        savedCategoryModel = addCategory();
        return self();
    }

    public CarGivenStage admin_saves_new_car() {
        user_is_logged_in_as_admin();
        savedCategoryModel = addCategory();
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carModel.setCategoryId(savedCategoryModel.getId());
        savedCarModel = carService.save(carModel, createMultipartFile());
        return self();
    }

    private CategoryModel addCategory() {
        return categoryService.save(createCategoryModel(), createMultipartFile());
    }

    public CarGivenStage admin_has_added_two_cars() {
        admin_saves_new_car();
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carModel.setCategoryId(savedCategoryModel.getId());
        carModel.setPlate("11111");
        savedCarModel = carService.save(carModel, createMultipartFile());
        return self();
    }

    public CarGivenStage a_car_has_an_active_reservation() {
        admin_saves_new_car();
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setCarId(savedCarModel.getId());
        reservationService.save(reservationModel);
        return self();
    }


}
