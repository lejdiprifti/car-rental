package com.ikubinfo.rental.service.email;

import com.ikubinfo.rental.service.reservation.dto.ReservationEntity;

public interface EmailService {

    void sendConfirmationMail(ReservationEntity reservationEntity, double fee);
    void sendCancelMail(ReservationEntity reservationEntity);
}
