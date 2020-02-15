package com.ikubinfo.rental.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.config.NonValidDataException;
import com.ikubinfo.rental.config.UserAlreadyExistsException;
import com.ikubinfo.rental.converter.UserConverter;
import com.ikubinfo.rental.entity.RoleEntity;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.model.ReservationModel;
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

	@Autowired
	private ReservationService reservationService;

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
			validateUserData(user);
			checkIfExists(user.getUsername());
			logger.info("Registering new user.");
			UserEntity entity = new UserEntity();
			entity.setFirstName(user.getFirstName().trim());
			entity.setLastName(user.getLastName().trim());
			if (user.getUsername().trim().length() > 0) {
			entity.setUsername(user.getUsername().trim());
			} else {
				throw new NonValidDataException("Username is required.");
			}
			entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
			entity.setEmail(user.getEmail().trim());
			entity.setPhone(user.getPhone().trim());
			entity.setAddress(user.getAddress().trim());
			entity.setBirthdate(user.getBirthdate());
			entity.setActive(true);
			RoleEntity role = new RoleEntity();
			role.setId(2);
			entity.setRole(role);
			userRepository.save(entity);
		} catch (UserAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void edit(UserModel user) {
		try {
			validateUserData(user);
			UserEntity entity = userRepository.getByUsername(jwtTokenUtil.getUsername());
			entity.setFirstName(user.getFirstName().trim());
			entity.setLastName(user.getLastName().trim());
			if (user.getPassword() != null) {
			entity.setPassword(passwordEncoder.encode(user.getPassword().trim()));
			}
			entity.setAddress(user.getAddress().trim());
			entity.setEmail(user.getEmail().trim());
			entity.setPhone(user.getPhone().trim());
			entity.setBirthdate(user.getBirthdate());
			userRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void delete() {
		try {
			logger.info("Deleting user.");
			UserEntity entity = userRepository.getByUsername(jwtTokenUtil.getUsername());
			entity.setActive(false);
			List<ReservationModel> resList = reservationService.getByUsername();
			for (ReservationModel res : resList) {
				res.setActive(false);
				reservationService.cancel(res.getId());
			}
			userRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}

	public void checkIfExists(String username) throws UserAlreadyExistsException {
		try {
			logger.info("Checking if user already exists.");
			userRepository.getByUsername(username);
			throw new UserAlreadyExistsException("User already exists.");
		} catch (NoResultException e) {
			logger.info("User is able to register.");
		}
	}

	public void validateUserData(UserModel model) throws NonValidDataException {
		try {
		if (model.getFirstName().trim().length() == 0) {
			throw new NonValidDataException("First name is required.");
		}
		if (model.getLastName().trim().length() == 0) {
			throw new NonValidDataException("Last name is required.");
		}
		if (model.getBirthdate() == null) {
			throw new NonValidDataException("Birthdate is required.");
		} else if (!isOver18(model.getBirthdate())) {
			throw new NonValidDataException("You must be over 18 years old.");
		}
		if (model.getAddress().trim().length() == 0) {
			throw new NonValidDataException("Address is required.");
		}
		if (model.getPhone().trim().length() == 0) {
			throw new NonValidDataException("Phone is required.");
		}
		if (model.getEmail().trim().length() == 0) {
			throw new NonValidDataException("Email is required.");
		}
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data are missing.");
		}
	}
	
	public boolean isOver18(LocalDateTime birthdate) {
		if (LocalDateTime.now().getYear() - birthdate.getYear() >= 18) {
			return true;
		} else {
			return false;
		}
	}
}
