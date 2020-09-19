package com.ikubinfo.rental.user;

import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.util.FakeUsers;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

}
