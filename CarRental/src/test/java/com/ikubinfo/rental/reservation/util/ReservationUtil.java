package com.ikubinfo.rental.reservation.util;

import com.ikubinfo.rental.model.ReservationModel;

import java.time.LocalDateTime;
import java.util.Date;

public class ReservationUtil {

    private ReservationUtil() {}

    public static ReservationModel createReservationModel() {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setStartDate(LocalDateTime.now());
        reservationModel.setEndDate(LocalDateTime.now().plusDays(1));
        reservationModel.setFee(200);
        reservationModel.setActive(true);
        return reservationModel;
    }
}
