package com.ikubinfo.rental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.security.JwtTokenUtil;

@Service
public class AuthorizationService {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public AuthorizationService() {
		
	}
	
	public boolean isUserAuthorized(){
		if ((int) jwtTokenUtil.getRole().get("id") == 1){
			return true;
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized.");
		}
	}
}
