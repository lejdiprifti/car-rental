package com.ikubinfo.rental.security.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
