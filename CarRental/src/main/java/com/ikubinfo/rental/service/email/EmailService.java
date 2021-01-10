package com.ikubinfo.rental.service.email;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.CarModel;

public interface EmailService {

    void sendConfirmationMail(ReservationEntity reservationEntity, double fee);
    void sendCancelMail(ReservationEntity reservationEntity);
}
