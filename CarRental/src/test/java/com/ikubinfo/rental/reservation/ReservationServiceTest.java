package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.util.FakeUsers;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationServiceTest extends SpringScenarioTest<ReservationGivenStage, ReservationWhenStage, ReservationThenStage> {

    @Test
    public void user_reserves_car_successfully() {
        given().admin_adds_an_available_car()
                .and()
                .user_is_logged_in_as_user();
        when().user_tries_to_reserve_car();
        then().there_are_exactly_$_reservations_of_user_$(1, FakeUsers.USER.username);
    }

    @Test
    public void user_cancel_reservation_successfully() {
        given().user_reserves_car();
        when().user_tries_to_cancel_reservation();
        then().there_are_exactly_$_reservations_of_user_$(0, FakeUsers.USER.username);
    }

    @Test
    public void user_updates_reservation_successfully() {
        given().user_reserves_car();
        when().user_tries_to_update_reservation();
        then().there_are_exactly_$_reservations_with_start_date_$(1, "2020-01-01 01:01");
    }
}
