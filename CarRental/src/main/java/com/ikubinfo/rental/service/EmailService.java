package com.ikubinfo.rental.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.model.Mail;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.UserRepository;

@Service
public class EmailService {
	
	private static Logger logger = LogManager.getLogger(EmailService.class);
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CarRepository carRepository;

	public void prepareAndSend(Mail mail, CarEntity car) throws MessagingException {
		logger.info("TRYING TO SEND MAIL");
		MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
	        Context context = new Context();
	        context.setVariables(mail.getContent());
	        String html = springTemplateEngine.process("mailTemplate", context);
	        messageHelper.setText(html, true);
	        messageHelper.addAttachment("logo.png", new ClassPathResource("car-rentals.png"), "image/png");
	        messageHelper.setFrom("ikubinfo.car.rentals@gmail.com");
	        messageHelper.setTo(mail.getTo());
	        messageHelper.setSubject(mail.getSubject());	
	        final InputStreamSource imageSource = new ByteArrayResource(car.getPhoto());
			messageHelper.addInline("imageResourceName", imageSource, "image/jpg");
	        mailSender.send(message);
	}
	
	public Mail setMailProperties(ReservationEntity reservation, double fee) {
		Mail mail = new Mail();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		mail.setFrom("ikubinfo.car.rentals@gmail.com");
		Map<String, Object> content = new HashMap<String, Object>();
		UserEntity user = userRepository.getById(reservation.getUserId());
		CarEntity car = carRepository.getById(reservation.getCarId());
		content.put("name", user.getFirstName() + ' ' + user.getLastName());
		content.put("carName", car.getName());
		content.put("carBrand", car.getType());
		content.put("carPlate", car.getPlate());
		content.put("price", fee);
		content.put("startDate", reservation.getStartDate().format(formatter));
		content.put("endDate", reservation.getEndDate().format(formatter));
		content.put("signature", "Car Rentals Albania");
		content.put("location", "Papa Gjon Pali 3rd St. , Tirana, Albania");
		mail.setContent(content);
		mail.setTo(user.getEmail());
		mail.setSubject("Receipt Confirmation");
		return mail;
	}
	
	
	public Mail setCancelMailProperties(ReservationEntity reservation) {
		Mail mail = new Mail();
		mail.setFrom("ikubinfo.car.rentals@gmail.com");
		Map<String, Object> content = new HashMap<String, Object>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		content.put("name", reservation.getUser().getFirstName() +" " + reservation.getUser().getLastName());
		content.put("carPlate", reservation.getCar().getPlate());
		content.put("carName", reservation.getCar().getName());
		content.put("startDate", reservation.getStartDate().format(formatter));
		content.put("endDate", reservation.getEndDate().format(formatter));
		content.put("signature", "Car Rentals Albania");
		content.put("location", "Papa Gjon Pali 3rd St. , Tirana, Albania");
		mail.setContent(content);
		mail.setTo(reservation.getUser().getEmail());
		mail.setSubject("Canceled Reservation");
		return mail;
	}
	
	public void sendCancelMail(Mail mail) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
	        Context context = new Context();
	        context.setVariables(mail.getContent());
	        String html = springTemplateEngine.process("cancelMail", context);
	        messageHelper.setText(html, true);
	        messageHelper.addAttachment("logo.png", new ClassPathResource("car-rentals.png"), "image/png");
	        messageHelper.setFrom("ikubinfo.car.rentals@gmail.com");
	        messageHelper.setTo(mail.getTo());
	        messageHelper.setSubject(mail.getSubject());	
	        mailSender.send(message);
	}
}