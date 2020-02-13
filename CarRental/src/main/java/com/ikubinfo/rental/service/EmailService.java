package com.ikubinfo.rental.service;

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

	public void prepareAndSend(Mail mail, byte[] imageBytes) throws MessagingException {
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
	        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
			messageHelper.addInline("imageResourceName", imageSource, "image/jpg");
	        mailSender.send(message);
	}
}