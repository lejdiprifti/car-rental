package com.ikubinfo.rental.reservation.util;

import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.UserModel;

import java.time.LocalDateTime;
import java.util.Date;

public class ReservationUtil {

    private ReservationUtil() {}

    public static ReservationModel createReservationModel() {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setStartDate(LocalDateTime.now().plusDays(1));
        reservationModel.setEndDate(LocalDateTime.now().plusDays(10));
        reservationModel.setFee(200);
        reservationModel.setActive(true);

        UserModel userModel = new UserModel();
        userModel.setId(1L);
        reservationModel.setUser(userModel);
        return reservationModel;
    }
}
