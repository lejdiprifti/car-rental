package com.ikubinfo.rental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ikubinfo.rental.entity.ReservationEntity;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	public EmailService() {
		
	}
	
	public void sendEmailTo(ReservationEntity reservation, double fee) {
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(reservation.getUser().getEmail()); 
        message.setSubject("Receipt Confirmation"); 
        message.setText("Dear "+reservation.getUser().getFirstName()+", \nYou successfully reserved a " + reservation.getCar().getName() + " - "
        		+ reservation.getCar().getType() + " during the following days:\n" + 
        		reservation.getStartDate() + " - " + reservation.getEndDate() + "\nwith  a total cost of " + fee +" lek.");
        emailSender.send(message);
	}
}
