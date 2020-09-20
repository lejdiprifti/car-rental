package com.ikubinfo.rental.user;

import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.ikubinfo.rental.user.util.UserUtil.USER_ID;
import static com.ikubinfo.rental.user.util.UserUtil.createUserModel;

@JGivenStage
public class UserWhenStage extends Stage<UserWhenStage> {

    @Autowired
    private UserService userService;

    @ProvidedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    public UserWhenStage user_tries_to_update_his_profile_with_new_username_$(String username) {
        UserModel userModel = createUserModel();
        userModel.setUsername(username);
        userService.edit(userModel);
        return self();
    }

    public UserWhenStage user_tries_to_delete_his_account() {
        userService.delete();
        return self();
    }

    public UserWhenStage new_user_with_username_$_tries_to_register(String username) {
        UserModel userModel = createUserModel();
        userModel.setUsername(username);
        userService.register(userModel);
        return self();
    }

    public void new_user_under_18_tries_to_register() {
        try {
            UserModel userModel = createUserModel();
            userModel.setBirthdate(LocalDateTime.now());
            userService.register(userModel);
        } catch (CarRentalBadRequestException exception) {
            carRentalBadRequestException = exception;
        }
    }

    public void user_tries_to_update_his_password_to_$(String updatedPassword) {
        UserModel userModel = createUserModel();
        userModel.setId(USER_ID);
        userModel.setPassword(updatedPassword);
        userService.edit(userModel);
    }

    public void user_tries_to_update_his_last_name(String updatedLastName) {
        UserModel userModel = createUserModel();
        userModel.setPassword(null);
        userModel.setId(USER_ID);
        userModel.setLastName(updatedLastName);
        userService.edit(userModel);
    }
}
