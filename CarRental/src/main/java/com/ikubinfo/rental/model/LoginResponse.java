package com.ikubinfo.rental.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
	
	private String jwt;
	private UserModel user;
	
}
