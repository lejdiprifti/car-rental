package com.ikubinfo.rental.service.email;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.CarModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class FakeEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakeEmailService.class);

    @Override
    public void sendConfirmationMail(ReservationEntity reservationEntity, double fee) {
        LOGGER.info("Sent confirmation email with reservationId {}, reservationFee {} and carId {}",
                reservationEntity.getId(), fee, reservationEntity.getCarId());
    }

    @Override
    public void sendCancelMail(ReservationEntity reservationEntity) {
        LOGGER.info("Sent cancellation email for reservationId {}",
                reservationEntity.getId());
    }
}
