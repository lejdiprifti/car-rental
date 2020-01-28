package com.ikubinfo.rental.resource;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;

@RestController
@RequestMapping(path="/user",produces="application/json")
@CrossOrigin("http://localhost:4200")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	public UserResource() {
		
	}
	
	@GetMapping
	public ResponseEntity<List<UserModel>> getAll(){
		return new ResponseEntity<List<UserModel>>(userService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> getById(@PathParam("id") Long id){
		return new ResponseEntity<UserModel>(userService.getById(id), HttpStatus.OK);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(UserModel user) {
		userService.edit(user);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete() {
		userService.delete();
	}
}
