package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.util.FakeUsers;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ikubinfo.rental.reservation.util.ReservationUtil.END_DATE;
import static com.ikubinfo.rental.reservation.util.ReservationUtil.START_DATE;

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
        when().user_tries_to_update_reservation_with_startDate(START_DATE);
        then().there_are_exactly_$_reservations_with_start_date_$(1, START_DATE);
    }

    @Test
    public void user_cannot_reserve_car_with_invalid_dates() {
        given().admin_adds_an_available_car()
                .and()
                .user_is_logged_in_as_user();
        when().user_tries_to_reserve_car_with_invalid_dates();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.INVALID_DATES.getErrorMessage());
    }

    @Test
    public void user_cannot_reserve_car_with_startDate_in_the_middle_of_previous_reservation() {
        given().user_reserves_car();
        when().user_tries_to_reserve_car_with_startDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_reserve_car_with_endDate_in_the_middle_of_previous_reservation() {
        given().user_reserves_car();
        when().user_tries_to_reserve_car_with_endDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_reserve_car_with_startDate_and_endDate_in_the_middle_of_previous_reservation() {
        given().user_reserves_car();
        when().user_tries_to_reserve_car_with_startDate_and_endDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_reserve_car_with_previous_reservation_dates_in_the_middle_of_the_new_dates() {
        given().user_reserves_car();
        when().user_tries_to_reserve_car_with_previous_reservation_dates_in_the_middle_of_the_new_dates();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_reserves_car_with_startDate_and_endDate_after_the_previous_reservation() {
        given().user_reserves_car();
        when().user_tries_to_reserve_car_with_startDate_and_endDate_after_the_previous_reservation();
        then().there_are_exactly_$_reservations_of_user_$(2, FakeUsers.USER.username);
    }

    @Test
    public void user_cannot_update_reservation_with_invalid_dates() {
        given().user_reserves_car();
        when().user_tries_to_update_reservation_with_invalid_dates();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.INVALID_DATES.getErrorMessage());
    }

    @Test
    public void user_cannot_update_reservation_with_startDate_in_the_middle_of_previous_reservation() {
        given().user_has_made_two_reservations();
        when().user_tries_to_update_reservation_with_startDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_update_reservation_with_endDate_in_the_middle_of_previous_reservation() {
        given().user_has_made_two_reservations();
        when().user_tries_to_update_reservation_with_endDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_update_reservation_with_startDate_and_endDate_in_the_middle_of_previous_reservation() {
        given().user_has_made_two_reservations();
        when().user_tries_to_update_reservation_with_startDate_and_endDate_in_the_middle_of_previous_reservation();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_cannot_update_reservation_with_previous_reservation_dates_in_the_middle_of_the_new_dates() {
        given().user_has_made_two_reservations();
        when().user_tries_to_update_reservation_with_previous_reservation_dates_in_the_middle_of_the_new_dates();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CAR_RESERVED.getErrorMessage());
    }

    @Test
    public void user_updates_reservation_with_dates_after_the_previous_reservation_dates() {
        given().user_has_made_two_reservations();
        when().user_tries_to_update_reservation_with_dates_after_the_previous_reservation_dates(END_DATE);
        then().there_are_exactly_$_reservations_of_user_$(2, FakeUsers.USER.username)
                .and()
                .there_are_exactly_$_reservations_with_end_date_$(1, END_DATE);
    }

    @Test
    public void unauthorized_user_cannot_cancel_reservation_of_another_user() {
        given().user_reserves_car()
                .and()
                .user_is_logged_in_as_admin();
        when().unauthorized_user_tries_to_cancel_reservation_of_another_user();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.UNAUTHORIZED.getErrorMessage());
    }

    @Test
    public void unauthorized_user_cannot_update_reservation_of_another_user(){
        given().user_reserves_car()
                .and()
                .user_is_logged_in_as_admin();
        when().unauthorized_user_tries_to_update_reservation_of_another_user();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.UNAUTHORIZED.getErrorMessage());
    }

    @Test
    public void admin_cancels_reservations_by_car_and_date() {
        given().user_has_made_two_reservations()
                .and()
                .user_is_logged_in_as_admin();
        when().admin_tries_to_cancel_reservations_by_car_and_date();
        then().there_are_exactly_$_reservations_of_user_$(1, FakeUsers.USER.username);
    }
}
