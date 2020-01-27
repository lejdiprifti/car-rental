package com.ikubinfo.rental.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.rental.model.LoginRequest;
import com.ikubinfo.rental.model.LoginResponse;
import com.ikubinfo.rental.service.LoginService;

@RestController
@RequestMapping(path="/login", produces="application/json")
@CrossOrigin("http://localhost:4200")
public class LoginResource {
	
	@Autowired
	private LoginService loginService;
	
	public LoginResource() {
		
	}
	
	@PostMapping(consumes="application/json")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception{
		return new ResponseEntity<LoginResponse>(loginService.authenticate(request), HttpStatus.OK);
	}
}
