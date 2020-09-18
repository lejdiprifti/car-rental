package com.ikubinfo.rental.car;


import com.ikubinfo.rental.CarRentalTest;
import com.tngtech.jgiven.integration.spring.junit5.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CarServiceTest extends SpringScenarioTest<CarGivenStage, CarWhenStage, CarThenStage> {

    @Test
    public void admin_adds_new_car_successfully() {
        when().admin_tries_to_add_new_car();
        then().there_are_exactly_$_cars(1);
    }
}
