package com.ikubinfo.rental.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	public BeanConfig() {

	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mm = new ModelMapper();
		mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mm;
	}
}
