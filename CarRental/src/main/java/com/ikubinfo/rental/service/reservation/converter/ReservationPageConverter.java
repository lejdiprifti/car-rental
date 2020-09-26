package com.ikubinfo.rental.service.reservation.converter;

import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.reservation.dto.ReservationPage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationPageConverter {

    public ReservationPage toModel(List<ReservationModel> reservationList, Long totalRecords) {
        ReservationPage reservationPage = new ReservationPage();
        reservationPage.setReservationList(reservationList);
        reservationPage.setTotalRecords(totalRecords);
        return reservationPage;
    }
}
