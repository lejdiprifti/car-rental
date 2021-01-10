package com.ikubinfo.rental;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJGiven
@ComponentScan(basePackages = {"com.ikubinfo"})
public class JGivenTestConfiguration {
}
