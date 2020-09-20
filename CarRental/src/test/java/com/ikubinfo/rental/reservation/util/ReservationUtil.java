package com.ikubinfo.rental.reservation.util;

import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.UserModel;

import java.time.LocalDateTime;

public class ReservationUtil {

    public static final LocalDateTime START_DATE = LocalDateTime.now().plusDays(2);
    public static final LocalDateTime END_DATE = LocalDateTime.now().plusDays(15);

    private ReservationUtil() {
    }

    public static ReservationModel createReservationModel() {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setStartDate(LocalDateTime.now().plusDays(1));
        reservationModel.setEndDate(LocalDateTime.now().plusDays(10));
        reservationModel.setFee(200);
        reservationModel.setActive(true);
        return reservationModel;
    }
}
