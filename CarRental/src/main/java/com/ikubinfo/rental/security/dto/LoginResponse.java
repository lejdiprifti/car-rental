package com.ikubinfo.rental.security.dto;

import com.ikubinfo.rental.service.user.dto.UserModel;
import lombok.Data;

@Data
public class LoginResponse {

    private String jwt;
    private UserModel user;

}
