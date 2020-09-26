package com.ikubinfo.rental.user;

import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.util.FakeUsers;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ikubinfo.rental.user.util.UserUtil.UPDATED_LASTNAME;
import static com.ikubinfo.rental.user.util.UserUtil.UPDATED_PASSWORD;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends SpringScenarioTest<UserGivenStage, UserWhenStage, UserThenStage> {

    @Test
    public void user_cannot_update_his_username() {
        given().user_is_logged_in_as_user();
        when().user_tries_to_update_his_profile_with_new_username_$("tester");
        then().there_are_exactly_$_users_with_username_$(1, FakeUsers.USER.username);
    }

    @Test
    public void user_deletes_account_successfully() {
        given().user_is_logged_in_as_user();
        when().user_tries_to_delete_his_account();
        then().there_are_exactly_$_users_with_username_$(0, FakeUsers.USER.username);
    }

    @Test
    public void new_user_registers_successfully() {
        when().new_user_with_username_$_tries_to_register("new user");
        then().there_are_exactly_$_users_with_username_$(1, "new user");
    }

    @Test
    public void all_reservations_made_by_user_are_canceled_when_user_deletes_account() {
        given().user_reserves_a_car();
        when().user_tries_to_delete_his_account();
        then().there_are_exactly_$_reservations_of_user_$(0, FakeUsers.USER.username)
                .and()
                .there_are_exactly_$_users_with_username_$(0, FakeUsers.USER.username);
    }

    @Test
    public void exception_is_thrown_if_user_is_under_18() {
        when().new_user_under_18_tries_to_register();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.MUST_BE_OVER_18.getErrorMessage());
    }

    @Test
    public void user_updates_his_password_successfully() {
        given().user_is_logged_in_as_user();
        when().user_tries_to_update_his_password_to_$(UPDATED_PASSWORD);
        then().the_password_of_user_$_is_updated_to_$(FakeUsers.USER.username, UPDATED_PASSWORD);
    }

    @Test
    public void user_updates_his_last_name_successfully() {
        given().user_is_logged_in_as_user();
        when().user_tries_to_update_his_last_name(UPDATED_LASTNAME);
        then().the_lastname_of_user_$_is_updated_to_$(FakeUsers.USER.username, UPDATED_LASTNAME)
                .and()
                .the_password_of_user_$_is_updated_to_$(FakeUsers.USER.username, "Admin123_");
    }

}
