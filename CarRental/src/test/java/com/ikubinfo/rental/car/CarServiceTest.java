package com.ikubinfo.rental.car;


import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.entity.StatusEnum;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CarServiceTest extends SpringScenarioTest<CarGivenStage, CarWhenStage, CarThenStage> {

    @Test
    public void admin_adds_new_car_successfully() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_add_new_car();
        then().there_are_exactly_$_cars_with_status_$(1, StatusEnum.AVAILABLE.name());
    }

    @Test
    public void admin_updates_car_successfully() {
        given().user_is_logged_in_as_admin()
                .and()
                .admin_saves_new_car();
        when().admin_tries_to_update_car();
        then().there_are_exactly_$_cars_with_status_$(1, StatusEnum.SERVIS.name());
    }

    @Test
    public void admin_deletes_car_successfully() {
        given().user_is_logged_in_as_admin()
                .and()
                .admin_saves_new_car();
        when().admin_tries_to_delete_car();
        then().there_are_exactly_$_cars(0);
    }
}
