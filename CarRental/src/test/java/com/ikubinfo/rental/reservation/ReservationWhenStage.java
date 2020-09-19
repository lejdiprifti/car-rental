package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.ReservationService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.ikubinfo.rental.reservation.util.ReservationUtil.createReservationModel;

@JGivenStage
public class ReservationWhenStage extends Stage<ReservationWhenStage> {

    @Autowired
    private ReservationService reservationService;

    @ExpectedScenarioState
    private CarModel savedCarModel;

    @ExpectedScenarioState
    private ReservationModel savedReservationModel;

    public ReservationWhenStage user_tries_to_reserve_car() {
        ReservationModel reservationModel = createReservationModel();
        reservationModel.setCarId(savedCarModel.getId());
        reservationService.save(reservationModel);
        return self();
    }

    public ReservationWhenStage user_tries_to_cancel_reservation() {
        reservationService.cancel(savedReservationModel.getId());
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation() {
        savedReservationModel.setStartDate(LocalDateTime.of(2020, 1,1,1,1));
        reservationService.edit(savedReservationModel, savedReservationModel.getId());
        return self();
    }
}
