package com.ikubinfo.rental.user;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.user.util.UserUtil.createUserModel;

@JGivenStage
public class UserWhenStage extends Stage<UserWhenStage> {

    @Autowired
    private UserService userService;

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
}
