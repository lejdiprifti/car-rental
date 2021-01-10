package com.ikubinfo.rental.security.model;

import com.ikubinfo.rental.model.UserModel;
import lombok.Data;

@Data
public class LoginResponse {

    private String jwt;
    private UserModel user;

}
