package com.ikubinfo.rental.model;

public class LoginResponse {
	
	private String jwt;
	private UserModel user;
	
	public LoginResponse() {
		
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "LoginResponse [jwt=" + jwt + ", user=" + user + "]";
	}
}
