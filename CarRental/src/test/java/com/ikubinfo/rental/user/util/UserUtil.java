package com.ikubinfo.rental.user.util;

import com.ikubinfo.rental.service.user.dto.UserModel;

import java.time.LocalDateTime;

public class UserUtil {

    public static final Long USER_ID = 2L;
    public static final String UPDATED_PASSWORD = "Admin123-";
    public static final String UPDATED_LASTNAME = "Updated Lastname";

    private UserUtil() {}

    public static UserModel createUserModel() {
        UserModel userModel = new UserModel();
        userModel.setFirstName("FirstName");
        userModel.setLastName("LastName");
        userModel.setAddress("Address");
        userModel.setBirthdate(LocalDateTime.of(1999, 1,1,1,1));
        userModel.setEmail("test@gmail.com");
        userModel.setPhone("1929192");
        userModel.setUsername("tester");
        userModel.setPassword("Admin123+");
        return userModel;
    }
}
