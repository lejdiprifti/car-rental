package com.ikubinfo.rental.service.email;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.Mail;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("!test")
public class ProductionEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionEmailService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Override
    public void sendConfirmationMail(ReservationEntity reservationEntity, double fee) {
        LOGGER.info("Sending confirmation mail");
        MimeMessage message = prepareMail(setMailProperties(reservationEntity, fee), "mailTemplate");
        mailSender.send(message);
    }

    private Mail setMailProperties(ReservationEntity reservation, double fee) {
        Mail mail = new Mail();
        mail.setFrom("ikubinfo.car.rentals@gmail.com");
        Map<String, Object> content = new HashMap<>();
        UserModel user = userService.getById(reservation.getUserId());
        CarModel car = carService.getById(reservation.getCarId());
        content.put("name", user.getFirstName() + ' ' + user.getLastName());
        content.put("carName", car.getName());
        content.put("carBrand", car.getType());
        content.put("carPlate", car.getPlate());
        content.put("price", fee);
        content.put("startDate", reservation.getStartDate().format(DATE_TIME_FORMATTER));
        content.put("endDate", reservation.getEndDate().format(DATE_TIME_FORMATTER));
        content.put("signature", "Car Rentals Albania");
        content.put("location", "Papa Gjon Pali 3rd St. , Tirana, Albania");
        mail.setContent(content);
        mail.setTo(user.getEmail());
        mail.setSubject("Receipt Confirmation");
        return mail;
    }

    @Override
    public void sendCancelMail(ReservationEntity reservationEntity) {
        LOGGER.info("Sending cancellation mail");
        MimeMessage message = prepareMail(setCancelMailProperties(reservationEntity), "cancelMail");
        mailSender.send(message);
    }

    private Mail setCancelMailProperties(ReservationEntity reservation) {
        Mail mail = new Mail();
        mail.setFrom("ikubinfo.car.rentals@gmail.com");
        Map<String, Object> content = new HashMap<>();
        content.put("name", reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName());
        content.put("carPlate", reservation.getCar().getPlate());
        content.put("carName", reservation.getCar().getName());
        content.put("startDate", reservation.getStartDate().format(DATE_TIME_FORMATTER));
        content.put("endDate", reservation.getEndDate().format(DATE_TIME_FORMATTER));
        content.put("signature", "Car Rentals Albania");
        content.put("location", "Papa Gjon Pali 3rd St. , Tirana, Albania");
        mail.setContent(content);
        mail.setTo(reservation.getUser().getEmail());
        mail.setSubject("Canceled Reservation");
        return mail;
    }

    private MimeMessage prepareMail(Mail mail, String templateName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            Context context = new Context();
            context.setVariables(mail.getContent());
            String html = springTemplateEngine.process(templateName, context);
            messageHelper.setText(html, true);
            messageHelper.addAttachment("logo.png", new ClassPathResource("static/img/car-rentals.png"), "image/png");
            messageHelper.setFrom("ikubinfo.car.rentals@gmail.com");
            messageHelper.setTo(mail.getTo());
            messageHelper.setSubject(mail.getSubject());
            return message;
        } catch (MessagingException e) {
            throw new CarRentalBadRequestException(BadRequest.EMAIL_SENDING_FAILED.getErrorMessage());
        }
    }
}