package com.ikubinfo.rental.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
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
	private boolean active;
}
