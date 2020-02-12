package com.ikubinfo.rental.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.ikubinfo.rental.model.Mail;

@Service
public class EmailService {
	
	private static Logger logger = LogManager.getLogger(EmailService.class);
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	public EmailService() {

	}

	public void prepareAndSend(Mail mail) throws MessagingException {
		logger.info("TRYING TO SEND MAIL");
		MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
	        Context context = new Context();
	        context.setVariables(mail.getContent());
	        messageHelper.addAttachment("logo.png", new ClassPathResource("car-rentals.png"));
	        messageHelper.setFrom("ikubinfo.car.rentals@gmail.com");
	        messageHelper.setTo(mail.getTo());
	        messageHelper.setSubject(mail.getSubject());	        
	        String html = springTemplateEngine.process("mailTemplate", context);
	        messageHelper.setText(html, true);
	        mailSender.send(message);
	}
}