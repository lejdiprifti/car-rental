package com.ikubinfo.rental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Configuration
public class SpringMailConfig {

	@Autowired
	private TemplateEngine templateEngine;
	
	public SpringMailConfig() {
		
	}
  

	public String build(String message) {
		Context context = new Context();
		context.setVariable("message", message);
		return templateEngine.process("mail/mailTemplate", context);
	}
}
