package com.ikubinfo.rental.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.ikubinfo.rental.security.JwtTokenUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	private static Logger logger = LogManager.getLogger(UserService.class);
	
	public UserService() {

	}
	
	public UserModel getById(Long id) {
		try {
			logger.info("Getting user by id.");
			return userConverter.toModel(userRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	public List<UserModel> getAll() {
			logger.info("Getting all users.");
			return userConverter.toModel(userRepository.getAll());
	}
	public UserModel getByUsername(String username) {
		try {
			logger.info("Getting user by username.");
			return userConverter.toModel(userRepository.getByUsername(username));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	
	public void register(UserModel user) {
		try {
			checkIfExists(user.getUsername());
			logger.info("Registering new user.");
			UserEntity entity = new UserEntity();
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			entity.setUsername(user.getUsername());
			entity.setPassword(passwordEncoder.encode(user.getPassword()));
			entity.setEmail(user.getEmail());
			entity.setPhone(user.getPhone());
			entity.setAddress(user.getAddress());
			entity.setBirthdate(user.getBirthdate());
			entity.setActive(true);
			RoleEntity role = new RoleEntity();
			role.setId(2);
			entity.setRole(role);
			userRepository.save(entity);
		} catch (Exception e) {
			logger.error("User already exists.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
		}
	}
	
	public void edit(UserModel user) {
		try {
		UserEntity entity = userRepository.getByUsername(jwtTokenUtil.getUsername());
		if (user.getFirstName()!= null) {
			entity.setFirstName(user.getFirstName());
		}
		if (user.getLastName() != null) {
			entity.setLastName(user.getLastName());
		}
		if (user.getPassword() != null) {
			entity.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		if (user.getAddress() != null) {
			entity.setAddress(user.getAddress());
		}
		if (user.getEmail() != null) {
			entity.setEmail(user.getEmail());
		}
		if (user.getPhone() != null) {
			entity.setPhone(user.getPhone());
		}
		if (user.getBirthdate() != null) {
			entity.setBirthdate(user.getBirthdate());
		}
		logger.info("Editing user.");
		userRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	
	public void delete() {
		try {
		logger.info("Deleting user.");
		UserEntity entity = userRepository.getByUsername(jwtTokenUtil.getUsername());
		entity.setActive(false);
		userRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	
	public void checkIfExists(String username) throws Exception {
		try {
			logger.info("Checking if user already exists.");
			userRepository.getByUsername(username);
			throw new Exception("User already exists.");
		}catch (NoResultException e) {
			
		}
	}
}
