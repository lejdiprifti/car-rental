package com.ikubinfo.rental.model;

import lombok.Data;

@Data
public class LoginResponse {

    private String jwt;
    private UserModel user;

}
