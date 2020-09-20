package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.ReservationService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
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

    @ProvidedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    public ReservationWhenStage user_tries_to_reserve_car() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now().plusDays(3));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_cancel_reservation() {
        reservationService.cancel(savedReservationModel.getId());
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_startDate(LocalDateTime startDate) {
        savedReservationModel.setStartDate(startDate);
        reservationService.edit(savedReservationModel, savedReservationModel.getId());
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_invalid_dates() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setCarId(savedCarModel.getId());
            reservationModel.setEndDate(LocalDateTime.now().minusDays(1));
            reservationModel.setStartDate(LocalDateTime.now());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_startDate_in_the_middle_of_previous_reservation() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now().plusDays(3));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_endDate_in_the_middle_of_previous_reservation() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now());
            reservationModel.setEndDate(LocalDateTime.now().plusDays(3));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_startDate_and_endDate_in_the_middle_of_previous_reservation() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now().plusDays(2));
            reservationModel.setEndDate(LocalDateTime.now().plusDays(3));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_startDate_and_endDate_after_the_previous_reservation() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now().plusDays(11));
            reservationModel.setEndDate(LocalDateTime.now().plusDays(12));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_reserve_car_with_previous_reservation_dates_in_the_middle_of_the_new_dates() {
        try {
            ReservationModel reservationModel = createReservationModel();
            reservationModel.setStartDate(LocalDateTime.now());
            reservationModel.setEndDate(LocalDateTime.now().plusDays(15));
            reservationModel.setCarId(savedCarModel.getId());
            reservationService.save(reservationModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_invalid_dates() {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().minusDays(1));
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_startDate_in_the_middle_of_previous_reservation() {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().plusDays(3));
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_endDate_in_the_middle_of_previous_reservation() {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().plusHours(2));
            savedReservationModel.setEndDate(LocalDateTime.now().plusDays(5));
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_startDate_and_endDate_in_the_middle_of_previous_reservation() {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().plusDays(3));
            savedReservationModel.setEndDate(LocalDateTime.now().plusDays(6));
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_previous_reservation_dates_in_the_middle_of_the_new_dates() {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().plusHours(1));
            savedReservationModel.setEndDate(LocalDateTime.now().plusDays(15));
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }

    public ReservationWhenStage user_tries_to_update_reservation_with_dates_after_the_previous_reservation_dates(LocalDateTime endDate) {
        try {
            savedReservationModel.setStartDate(LocalDateTime.now().plusDays(13));
            savedReservationModel.setEndDate(endDate);
            reservationService.edit(savedReservationModel, savedReservationModel.getId());
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
        return self();
    }
}
