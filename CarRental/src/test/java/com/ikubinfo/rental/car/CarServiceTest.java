package com.ikubinfo.rental.car;


import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ikubinfo.rental.car.util.CarUtils.NON_EXISTING_CAR_ID;
import static com.ikubinfo.rental.car.util.CarUtils.NON_EXISTING_CATEGORY_ID;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CarServiceTest extends SpringScenarioTest<CarGivenStage, CarWhenStage, CarThenStage> {

    @Test
    public void admin_adds_new_car_successfully() {
        given().a_category_is_added();
        when().admin_tries_to_add_new_car();
        then().there_are_exactly_$_cars_with_status_$(1, StatusEnum.AVAILABLE.name());
    }

    @Test
    public void admin_updates_car_successfully() {
        given().admin_saves_new_car();
        when().admin_tries_to_update_car();
        then().there_are_exactly_$_cars_with_status_$(1, StatusEnum.SERVIS.name());
    }

    @Test
    public void admin_deletes_car_successfully() {
        given().admin_saves_new_car();
        when().admin_tries_to_delete_car();
        then().there_are_exactly_$_cars(0);
    }

    @Test
    public void admin_cannot_add_car_with_missing_data() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_add_new_car_with_missing_data();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.AVAILABILITY_REQUIRED.getErrorMessage());
    }

    @Test
    public void exception_is_thrown_for_non_existing_car() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_get_car_by_id_$(NON_EXISTING_CAR_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CAR_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void admin_cannot_add_two_cars_with_same_plate() {
        given().admin_saves_new_car();
        when().admin_tries_to_add_new_car();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
    }

    @Test
    public void admin_cannot_add_car_with_non_existing_category() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_add_car_with_non_existing_category(NON_EXISTING_CATEGORY_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void admin_cannot_update_car_with_an_existing_plate() {
        given().admin_has_added_two_cars();
        when().admin_tries_to_update_car();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
    }

    @Test
    public void an_exception_is_thrown_when_admin_tries_to_update_non_existing_car() {
        given().a_category_is_added();
        when().admin_tries_to_update_non_existing_car_with_id_$(NON_EXISTING_CAR_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CAR_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void an_exception_is_thrown_when_admin_tries_to_delete_non_existing_car() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_delete_car_with_id_$(NON_EXISTING_CAR_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CAR_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void admin_cannot_delete_car_with_active_reservations() {
        given().a_car_has_an_active_reservation();
        when().admin_tries_to_delete_car();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_HAS_ACTIVE_RESERVATIONS.getErrorMessage());
    }
}
