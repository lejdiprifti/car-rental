package com.ikubinfo.rental.car;

import com.ikubinfo.rental.service.CarService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

@JGivenStage
public class CarWhenStage extends Stage<CarWhenStage> {

    @Autowired
    private CarService carService;

    public CarWhenStage admin_tries_to_add_new_car() {
        CarEntity carEntity =
    }
}
