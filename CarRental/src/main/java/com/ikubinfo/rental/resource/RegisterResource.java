package com.ikubinfo.rental.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;

@RestController
@RequestMapping(path="/register", produces="application/json")
@CrossOrigin("http://localhost:4200")
public class RegisterResource {
	
	@Autowired
	private UserService userService;
	
	public RegisterResource() {
		
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void register(@RequestBody UserModel user) {
		userService.register(user);
	}
}
