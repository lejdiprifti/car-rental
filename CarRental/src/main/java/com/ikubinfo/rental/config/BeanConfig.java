package com.ikubinfo.rental.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
	
	public BeanConfig() {
		
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
