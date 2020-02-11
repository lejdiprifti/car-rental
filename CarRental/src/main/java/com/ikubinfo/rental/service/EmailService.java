package com.ikubinfo.rental.service;

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
	private MailContentBuilder mailBuilder;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	public EmailService() {

	}

	public void prepareAndSend(Mail mail) {
		logger.info("TRYING TO SEND MAIL");
	    MimeMessagePreparator messagePreparator = mimeMessage -> {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	        Context context = new Context();
	        context.setVariables(mail.getContent());
	        messageHelper.addAttachment("logo.png", new ClassPathResource("car-rentals.png"));
	        messageHelper.setFrom("ikubinfo.car.rentals@gmail.com");
	        messageHelper.setTo(mail.getTo());
	        messageHelper.setSubject(mail.getSubject());	        
	        String html = springTemplateEngine.process("mailTemplate", context);
	        messageHelper.setText(html, true);
	    };
	    try {
	        mailSender.send(messagePreparator);
	    } catch (MailException e) {
	        // runtime exception; compiler will not force you to handle it
	    }
	}
}