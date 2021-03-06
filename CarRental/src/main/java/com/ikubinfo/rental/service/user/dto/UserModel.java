package com.ikubinfo.rental.service.user.dto;

import com.ikubinfo.rental.service.role.dto.RoleModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserModel {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime birthdate;
    private String email;
    private String address;
    private String phone;
    private RoleModel role;
    private Long roleId;
    private boolean active;
}
