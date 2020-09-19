package com.ikubinfo.rental.user.util;

import com.ikubinfo.rental.model.RoleModel;
import com.ikubinfo.rental.model.UserModel;

import java.time.LocalDateTime;

public class UserUtil {

    private UserUtil() {}

    public static UserModel createUserModel() {
        UserModel userModel = new UserModel();
        userModel.setFirstName("FirstName");
        userModel.setLastName("LastName");
        userModel.setAddress("Address");
        userModel.setBirthdate(LocalDateTime.of(1999, 1,1,1,1));
        userModel.setEmail("test@gmail.com");
        userModel.setPhone("1929192");
        userModel.setUsername("test");
        userModel.setPassword("Admin123+");
        return userModel;
    }
}
