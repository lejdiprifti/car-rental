package com.ikubinfo.rental.service;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.converter.UserConverter;
import com.ikubinfo.rental.entity.RoleEntity;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private PasswordEncoder passwordEncoder;
	public UserService() {

	}

	public UserModel getByUsername(String username) {
		try {
			return userConverter.toModel(userRepository.getByUsername(username));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	
	public void register(UserModel user) {
		try {
			userRepository.getByUsername(user.getUsername());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken.");
		} catch (NoResultException e) {
		UserEntity entity = new UserEntity();
		entity.setFirstName(user.getFirstName());
		entity.setLastName(user.getLastName());
		entity.setUsername(user.getUsername());
		entity.setPassword(passwordEncoder.encode(user.getPassword()));
		entity.setEmail(user.getEmail());
		entity.setPhone(user.getPhone());
		entity.setAddress(user.getAddress());
		entity.setActive(true);
		RoleEntity role = new RoleEntity();
		role.setId(2);
		entity.setRole(role);
		userRepository.save(entity);
		}
	}
}
