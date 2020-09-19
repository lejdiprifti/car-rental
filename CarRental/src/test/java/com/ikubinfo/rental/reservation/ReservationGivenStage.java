package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.ReservationService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
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

    @ProvidedScenarioState
    private CarModel carModel;

    @ProvidedScenarioState
    private ReservationModel savedReservationModel;

    public ReservationGivenStage user_is_logged_in_as_user() {
        tokenCreator.createUserToken();
        return self();
    }

    public ReservationGivenStage admin_adds_an_available_car() {
        tokenCreator.createAdminToken();
        carModel = carService.save(createCarModelWithStatus(StatusEnum.AVAILABLE), createMultipartFile());
        return self();
    }

    public ReservationGivenStage user_reserves_car() {
        admin_adds_an_available_car();
        user_is_logged_in_as_user();
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setCarId(carModel.getId());
        savedReservationModel = reservationService.save(reservationModel);
        return self();
    }
}
